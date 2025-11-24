package com.unicamp.dslearn.domain.auth

import com.unicamp.dslearn.data.repository.auth.AuthRepository

class SignInUseCaseImpl(private val authRepository: AuthRepository) :
    SignInUseCase {

    override suspend fun invoke(token: String) {
        authRepository.signIn(token)
    }
}