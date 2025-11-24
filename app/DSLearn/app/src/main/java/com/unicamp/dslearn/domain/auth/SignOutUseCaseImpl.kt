package com.unicamp.dslearn.domain.auth

import com.unicamp.dslearn.data.repository.auth.AuthRepository

class SignOutUseCaseImpl(private val authRepository: AuthRepository) : SignOutUseCase {
    override fun invoke() {
        authRepository.signOut()
    }

}