package com.unicamp.dslearn.data.repository.auth

import com.unicamp.dslearn.core.model.UserModel
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun signIn(googleIdToken: String)

    fun getToken(): StateFlow<String?>

    fun isSignedIn(): Boolean

    fun signOut()

}