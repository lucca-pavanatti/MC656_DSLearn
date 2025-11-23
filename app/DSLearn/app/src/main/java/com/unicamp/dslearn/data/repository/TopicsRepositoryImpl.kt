package com.unicamp.dslearn.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.data.datasource.remote.TopicsPagingSource
import com.unicamp.dslearn.data.datasource.remote.TopicsPagingSource.Companion.TOPICS_PAGE_SIZE
import com.unicamp.dslearn.data.datasource.remote.api.TopicsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class TopicsRepositoryImpl(private val topicsApi: TopicsApi) : TopicsRepository {

    override fun getTopics(name: String): Flow<PagingData<TopicModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = TOPICS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TopicsPagingSource(topicsApi, name) },
        ).flow.map { pagingData ->
            pagingData.map {
                it.toModel()
            }
        }
    }
}