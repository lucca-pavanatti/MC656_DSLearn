package com.unicamp.dslearn.domain.topics

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.TopicModel
import kotlinx.coroutines.flow.Flow

fun interface SetTopicAsCompletedUseCase {
    suspend operator fun invoke(name: String)
}