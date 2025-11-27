package com.unicamp.dslearn.domain.user

import com.unicamp.dslearn.core.model.UserModel
import com.unicamp.dslearn.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCaseImpl(private val userRepository: UserRepository) :
    GetCurrentUserUseCase {
    override fun invoke(): Flow<UserModel?> {
        return userRepository.getCurrentUser()
    }
}