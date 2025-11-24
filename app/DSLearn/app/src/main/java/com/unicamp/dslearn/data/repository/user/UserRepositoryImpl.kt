package com.unicamp.dslearn.data.repository.user

import com.unicamp.dslearn.core.model.UserMetricsModel
import com.unicamp.dslearn.core.model.UserModel
import com.unicamp.dslearn.data.datasource.remote.api.UserApi
import com.unicamp.dslearn.data.fold
import com.unicamp.dslearn.data.repository.auth.TokenManager

class UserRepositoryImpl(
    private val tokenManager: TokenManager,
    private val userApi: UserApi
) : UserRepository {

    override suspend fun getCurrentUser(): UserModel? {
        val token = tokenManager.getToken() ?: return null

        return userApi.getCurrentUser(token).fold({
            it?.toModel()
        }, {
            null
        })
    }

    override suspend fun getUserMetrics(userId: Int): UserMetricsModel? {
        return userApi.getUserMetrics(userId).fold({
            it?.toModel()
        }, {
            null
        })
    }

}