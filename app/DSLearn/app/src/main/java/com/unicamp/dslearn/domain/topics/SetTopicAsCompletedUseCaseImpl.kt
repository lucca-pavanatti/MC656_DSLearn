package com.unicamp.dslearn.domain.topics

import com.unicamp.dslearn.data.repository.topics.TopicsRepository

class SetTopicAsCompletedUseCaseImpl(private val topicsRepository: TopicsRepository) :
    SetTopicAsCompletedUseCase {
    override suspend fun invoke(name: String) {
        topicsRepository.setTopicAsCompleted(name)
    }
}