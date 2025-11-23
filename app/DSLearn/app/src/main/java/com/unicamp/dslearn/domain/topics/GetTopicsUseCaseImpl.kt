package com.unicamp.dslearn.domain.topics

import com.unicamp.dslearn.data.repository.topics.TopicsRepository

internal class GetTopicsUseCaseImpl(private val topicsRepository: TopicsRepository) :
    GetTopicsUseCase {

    override fun invoke(name: String) = topicsRepository.getTopics(name)
}