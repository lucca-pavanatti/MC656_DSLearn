package com.unicamp.dslearn.data.repository.topics

import android.annotation.SuppressLint
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class TopicsRepositoryImpl(
    private val topicsApi: TopicsApi,
    private val topicsDao: TopicsDao
) : TopicsRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("CheckResult")
    override fun getTopics(name: String): Flow<PagingData<TopicModel>> {
        return topicsDao.getAll().flatMapLatest { localTopics ->
            Pager(
                config = PagingConfig(
                    pageSize = TOPICS_PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { TopicsPagingSource(topicsApi, name) },
            ).flow.map { pagingData ->
                val completedList = localTopics.filter {
                    it.completed
                }.associateBy {
                    it.name
                }

                var index = 0
                pagingData.map { topic ->
                    var unlocked = false
                    if (index == 0 || index <= completedList.size) {
                        unlocked = true
                    }

                    val topicEntity = TopicEntity(
                        name = topic.name,
                        content = topic.contentMarkdown,
                        completed = completedList[topic.name]?.let { true } ?: false,
                        unlocked = unlocked
                    )

                    topicsDao.insertAll(topicEntity)
                    index++

                    topic.toModel(completed = topicEntity.completed, topicEntity.unlocked)
                }
            }
        }
    }

    override suspend fun setTopicAsCompleted(name: String) {
        topicsDao.updateCompleted(name, true)
    }
}