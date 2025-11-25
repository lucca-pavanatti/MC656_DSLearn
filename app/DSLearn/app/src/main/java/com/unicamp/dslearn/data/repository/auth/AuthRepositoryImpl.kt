package com.unicamp.dslearn.data.repository.auth

import com.unicamp.dslearn.data.datasource.remote.api.AuthApi
import com.unicamp.dslearn.data.datasource.remote.dto.AuthDTO
import com.unicamp.dslearn.data.fold
import com.unicamp.dslearn.data.repository.sync.SyncRepository
import kotlinx.coroutines.flow.StateFlow

class AuthRepositoryImpl(
    private val tokenManager: TokenManager,
    private val authApi: AuthApi,
    private val syncRepository: SyncRepository,
) : AuthRepository {

    override suspend fun signIn(googleIdToken: String) {
        tokenManager.saveToken(token = googleIdToken)

        return authApi.auth(AuthDTO(googleIdToken)).fold({
            syncRepository.uploadUserProgress()
            tokenManager.saveToken(token = googleIdToken)
        }, { })
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