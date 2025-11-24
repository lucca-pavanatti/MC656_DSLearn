package com.unicamp.dslearn.data.repository.auth

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TokenManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        AUTH_TOKENS,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _token =
        MutableStateFlow(encryptedPrefs.getString(GOOGLE_ID_TOKEN, null))


    fun saveToken(token: String) {
        encryptedPrefs.edit {
            putString(GOOGLE_ID_TOKEN, token)
                .putLong(TIMESTAMP, System.currentTimeMillis())
        }
        _token.value = token
    }

    fun getToken(): StateFlow<String?> = _token.asStateFlow()


    fun clearToken() {
        encryptedPrefs.edit { clear() }
    }

    companion object {
        private const val AUTH_TOKENS = "auth_tokens"
        private const val GOOGLE_ID_TOKEN = "google_id_token"
        private const val TIMESTAMP = "timestamp"

    }
}