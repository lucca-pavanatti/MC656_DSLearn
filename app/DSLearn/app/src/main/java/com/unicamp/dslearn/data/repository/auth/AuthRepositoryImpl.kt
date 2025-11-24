package com.unicamp.dslearn.data.repository.auth

import com.unicamp.dslearn.core.model.UserModel
import com.unicamp.dslearn.data.datasource.local.dao.TopicsDao
import com.unicamp.dslearn.data.datasource.remote.api.AuthApi
import com.unicamp.dslearn.data.datasource.remote.api.UserApi
import com.unicamp.dslearn.data.datasource.remote.dto.AuthDTO
import com.unicamp.dslearn.data.datasource.remote.dto.TopicsStatusDTO
import com.unicamp.dslearn.data.fold
import com.unicamp.dslearn.data.repository.user.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthRepositoryImpl(
    private val tokenManager: TokenManager,
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val topicsDao: TopicsDao
) : AuthRepository {

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun signIn(googleIdToken: String): UserModel? {
        tokenManager.saveToken(token = googleIdToken)

        return authApi.auth(AuthDTO(googleIdToken)).fold({
            userApi.getCurrentUser(googleIdToken).fold({ userDto ->
                val user = userDto?.toModel()

                user?.let { user ->
                    coroutineScope.launch {
                        topicsDao.getAll().collect { topicList ->
                            topicList.forEach { topic ->
                                if (topic.completed) {
                                    userApi.updateTopic(
                                        user.id, TopicsStatusDTO(
                                            "COMPLETED",
                                            topic.name
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                user
            }, {
                null
            })
        }, {
            null
        })
    }

    override fun getToken(): StateFlow<String?> {
        return tokenManager.getToken()
    }

    override fun signOut() {
        tokenManager.clearToken()
    }

    override fun isSignedIn(): Boolean {
        return tokenManager.getToken().value != null
    }
}