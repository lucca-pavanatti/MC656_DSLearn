package com.unicamp.dslearn.data.repository.topics

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.TopicModel
import kotlinx.coroutines.flow.Flow

interface TopicsRepository {

    fun getTopics(name: String): Flow<PagingData<TopicModel>>

    suspend fun setTopicAsCompleted(name: String)

}
