package com.unicamp.dslearn.presentation.screens.account.uistate

import com.unicamp.dslearn.core.model.UserMetricsModel

sealed class AccountUiState {

    data object Loading : AccountUiState()

    data object SignedOut : AccountUiState()

    data class SignedIn(
        val userName: String,
        val userEmail: String,
        val userMetrics: UserMetricsModel
    ) : AccountUiState()
}

sealed class AccountUiEvent {

    data object SignInSuccess : AccountUiEvent()

    data object SignInCancelled : AccountUiEvent()

    data class SignInError(val errorType: SignInErrorType) : AccountUiEvent()

    data object SignOutSuccess : AccountUiEvent()
}

