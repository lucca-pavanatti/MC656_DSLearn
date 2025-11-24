package com.unicamp.dslearn.data.repository.auth

import com.unicamp.dslearn.core.model.UserModel
import com.unicamp.dslearn.data.datasource.remote.api.AuthApi
import com.unicamp.dslearn.data.datasource.remote.api.UserApi
import com.unicamp.dslearn.data.datasource.remote.dto.AuthDTO
import com.unicamp.dslearn.data.fold
import com.unicamp.dslearn.data.repository.user.toModel

class AuthRepositoryImpl(
    private val tokenManager: TokenManager,
    private val authApi: AuthApi,
    private val userApi: UserApi
) : AuthRepository {

    override suspend fun signIn(googleIdToken: String): UserModel? {
        tokenManager.saveToken(token = googleIdToken)

        return authApi.auth(AuthDTO(googleIdToken)).fold({
            userApi.getCurrentUser(googleIdToken).fold({
                it?.toModel()
            }, {
                null
            })
        }, {
            null
        })
    }

    override fun getToken(): String? {
        return tokenManager.getToken()
    }

    override fun signOut() {
        tokenManager.clearToken()
    }

    override fun isSignedIn(): Boolean {
        return tokenManager.getToken() != null
    }
}