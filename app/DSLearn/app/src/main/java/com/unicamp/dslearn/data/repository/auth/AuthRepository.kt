package com.unicamp.dslearn.data.repository.auth

import com.unicamp.dslearn.core.model.UserModel

interface AuthRepository {
    suspend fun signIn(googleIdToken: String): UserModel?

    fun getToken(): String?

    fun isSignedIn(): Boolean

    fun signOut()

}