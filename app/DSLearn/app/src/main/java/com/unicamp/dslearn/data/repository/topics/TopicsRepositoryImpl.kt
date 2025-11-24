package com.unicamp.dslearn.data.repository.topics

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.data.datasource.local.dao.TopicsDao
import com.unicamp.dslearn.data.datasource.local.entities.TopicEntity
import com.unicamp.dslearn.data.datasource.remote.TopicsPagingSource
import com.unicamp.dslearn.data.datasource.remote.TopicsPagingSource.Companion.TOPICS_PAGE_SIZE
import com.unicamp.dslearn.data.datasource.remote.api.TopicsApi
import com.unicamp.dslearn.data.fold
import com.unicamp.dslearn.data.repository.user.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class TopicsRepositoryImpl(
    private val topicsApi: TopicsApi,
    private val topicsDao: TopicsDao,
    private val userRepository: UserRepository,
) : TopicsRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTopics(name: String): Flow<PagingData<TopicModel>> {
        return userRepository.getCurrentUser()
            .flatMapLatest { user ->
                if (user == null) {
                    topicsDao.getAll().flatMapLatest { localTopics ->
                        createPagingFlow(name, localTopics, emptyList())
                    }
                } else {
                    flow {
                        val completedTopics = topicsApi.getCompletedTopics(user.id).fold(
                            onSuccess = { list ->
                                list?.filter { it.status == "COMPLETED" }
                                    ?.map { it.name }
                                    ?: emptyList()
                            },
                            onFailure = { emptyList() }
                        )
                        emit(completedTopics)
                    }.flatMapLatest { completedTopics ->
                        topicsDao.getAll().flatMapLatest { localTopics ->
                            createPagingFlow(name, localTopics, completedTopics)
                        }
                    }
                }
            }
    }

    private fun createPagingFlow(
        name: String,
        localTopics: List<TopicEntity>,
        completedTopics: List<String>
    ): Flow<PagingData<TopicModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = TOPICS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TopicsPagingSource(topicsApi, name) },
        ).flow.map { pagingData ->
            val completedList = localTopics.filter {
                it.completed || it.name in completedTopics
            }.associateBy { it.name }

            var index = 0
            pagingData.map { topic ->
                val unlocked = index == 0 || index <= completedList.size

                val topicEntity = TopicEntity(
                    name = topic.name,
                    content = topic.contentMarkdown,
                    completed = completedList[topic.name] != null,
                    unlocked = unlocked
                )

                topicsDao.insertAll(topicEntity)
                index++

                topic.toModel(
                    completed = topicEntity.completed,
                    unlocked = topicEntity.unlocked
                )
            }
        }
    }

    override suspend fun setTopicAsCompleted(name: String) {
        topicsDao.updateCompleted(name, true)
    }
}