package com.unicamp.dslearn.presentation.screens.account.uistate

sealed class SignInResult {
    data class Success(
        val idToken: String,
        val displayName: String?,
        val email: String?,
    ) : SignInResult()

    data object Cancelled : SignInResult()

    data class Error(val errorType: SignInErrorType) : SignInResult()
}

enum class SignInErrorType {
    NO_ACCOUNT,
    NETWORK_ERROR,
    TIMEOUT,
    PARSING_ERROR,
    AUTHENTICATION_FAILED,
    UNKNOWN
}