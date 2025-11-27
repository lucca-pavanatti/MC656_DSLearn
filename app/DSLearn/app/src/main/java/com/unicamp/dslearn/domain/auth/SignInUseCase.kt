package com.unicamp.dslearn.domain.auth

fun interface SignInUseCase {
    suspend operator fun invoke(token: String)
}