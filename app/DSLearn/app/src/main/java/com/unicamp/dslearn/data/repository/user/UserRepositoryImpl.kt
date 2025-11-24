package com.unicamp.dslearn.data.repository.user

import com.unicamp.dslearn.core.model.UserMetricsModel
import com.unicamp.dslearn.core.model.UserModel
import com.unicamp.dslearn.data.datasource.remote.api.UserApi
import com.unicamp.dslearn.data.fold
import com.unicamp.dslearn.data.repository.auth.TokenManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val tokenManager: TokenManager,
    private val userApi: UserApi,
) : UserRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentUser(): Flow<UserModel?> =
        tokenManager.getToken().flatMapLatest { token ->
            flow {
                if (token == null) {
                    emit(null)
                    return@flow
                }

                emit(
                    userApi.getCurrentUser(token).fold(
                        {
                            it?.toModel()
                        }, {
                            null
                        }
                    )
                )
            }
        }

    override suspend fun getUserMetrics(userId: Int): UserMetricsModel? {
        return userApi.getUserMetrics(userId).fold({
            it?.toModel()
        }, {
            null
        })
    }

}