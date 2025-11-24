package com.unicamp.dslearn.data.repository.user

import com.unicamp.dslearn.core.model.UserMetricsModel
import com.unicamp.dslearn.core.model.UserModel

interface UserRepository {

    suspend fun getCurrentUser(): UserModel?
    suspend fun getUserMetrics(userId: Int): UserMetricsModel?
}