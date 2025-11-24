package com.unicamp.dslearn.presentation.screens.account.uistate

sealed class AccountUiState {

    data object Loading : AccountUiState()

    data object SignedOut : AccountUiState()

    data class SignedIn(
        val userName: String,
        val userEmail: String,
        val topicsCompleted: Int = 0,
        val topicsInProgress: Int = 0,
        val exercisesCompleted: Int = 0
    ) : AccountUiState()
}

sealed class AccountUiEvent {

    data object SignInSuccess : AccountUiEvent()

    data object SignInCancelled : AccountUiEvent()

    data class SignInError(val errorType: SignInErrorType) : AccountUiEvent()

    data object SignOutSuccess : AccountUiEvent()
}

