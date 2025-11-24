package com.unicamp.dslearn.domain.user

import com.unicamp.dslearn.core.model.UserModel

fun interface GetCurrentUserUseCase {
    suspend operator fun invoke(): UserModel?
}