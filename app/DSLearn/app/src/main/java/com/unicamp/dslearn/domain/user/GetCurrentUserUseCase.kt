package com.unicamp.dslearn.domain.user

import com.unicamp.dslearn.core.model.UserModel
import kotlinx.coroutines.flow.Flow

fun interface GetCurrentUserUseCase {
    operator fun invoke(): Flow<UserModel?>
}