package com.unicamp.dslearn.data.repository.user

import com.unicamp.dslearn.core.model.UserMetricsModel
import com.unicamp.dslearn.core.model.UserModel
import com.unicamp.dslearn.data.datasource.remote.api.UserApi
import com.unicamp.dslearn.data.datasource.remote.dto.ExerciseStatusDTO
import com.unicamp.dslearn.data.datasource.remote.dto.TopicStatusDTO
import com.unicamp.dslearn.data.fold
import com.unicamp.dslearn.data.repository.auth.TokenManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
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

    override suspend fun getUserMetrics(): UserMetricsModel? {
        return getCurrentUser().first()?.let { user ->
            userApi.getUserMetrics(user.id).fold({
                it?.toModel()
            }, {
                null
            })
        }

    }

    override suspend fun setTopicAsCompleted(name: String) {
        getCurrentUser().collectLatest { user ->
            user?.let {
                userApi.updateTopic(
                    it.id,
                    TopicStatusDTO(
                        "COMPLETED",
                        name
                    )
                )
            }
        }
    }

    override suspend fun setExerciseAsCompleted(exerciseId: Int) {
        getCurrentUser().collectLatest { user ->
            user?.let {
                userApi.updateExercise(
                    it.id,
                    ExerciseStatusDTO(
                        "COMPLETED",
                        exerciseId
                    )
                )
            }
        }
    }

}