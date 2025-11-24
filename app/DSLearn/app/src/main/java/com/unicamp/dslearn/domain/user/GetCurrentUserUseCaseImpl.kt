package com.unicamp.dslearn.domain.user

import com.unicamp.dslearn.core.model.UserModel
import com.unicamp.dslearn.data.repository.user.UserRepository

class GetCurrentUserUseCaseImpl(private val userRepository: UserRepository) :
    GetCurrentUserUseCase {
    override suspend fun invoke(): UserModel? {
        return userRepository.getCurrentUser()
    }
}