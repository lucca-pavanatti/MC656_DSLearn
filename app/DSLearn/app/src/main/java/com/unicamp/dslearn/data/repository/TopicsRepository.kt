package com.unicamp.dslearn.data.repository

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.TopicModel
import kotlinx.coroutines.flow.Flow

fun interface TopicsRepository {

    fun getTopics(name: String): Flow<PagingData<TopicModel>>

}
