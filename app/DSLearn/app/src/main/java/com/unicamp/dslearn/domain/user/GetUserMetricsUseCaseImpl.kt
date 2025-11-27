package com.unicamp.dslearn.domain.user

import com.unicamp.dslearn.core.model.UserMetricsModel
import com.unicamp.dslearn.data.repository.user.UserRepository

class GetUserMetricsUseCaseImpl(private val userRepository: UserRepository) :
    GetUserMetricsUseCase {

    override suspend fun invoke(): UserMetricsModel? {
        return userRepository.getUserMetrics()
    }
}