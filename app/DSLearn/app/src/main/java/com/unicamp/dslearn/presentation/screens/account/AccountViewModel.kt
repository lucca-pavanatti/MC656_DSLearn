package com.unicamp.dslearn.presentation.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicamp.dslearn.domain.auth.SignInUseCase
import com.unicamp.dslearn.domain.auth.SignOutUseCase
import com.unicamp.dslearn.domain.user.GetCurrentUserUseCase
import com.unicamp.dslearn.domain.user.GetUserMetricsUseCase
import com.unicamp.dslearn.presentation.screens.account.uistate.AccountUiEvent
import com.unicamp.dslearn.presentation.screens.account.uistate.AccountUiState
import com.unicamp.dslearn.presentation.screens.account.uistate.SignInResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserMetricsUseCase: GetUserMetricsUseCase
) : ViewModel() {

    private var userId: Int? = null

    private val _uiState = MutableStateFlow<AccountUiState>(AccountUiState.SignedOut)
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AccountUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            getCurrentUserUseCase()?.apply {
                userId = id
                val userMetrics = getUserMetricsUseCase(id)

                _uiState.value = AccountUiState.SignedIn(
                    userName = name,
                    userEmail = email,
                    topicsCompleted = userMetrics?.completedTopics ?: 0,
                    topicsInProgress = userMetrics?.inProgressTopics ?: 0,
                    exercisesCompleted = userMetrics?.completedExercises ?: 0
                )
            }
        }
    }

    fun setLoading() {
        _uiState.value = AccountUiState.Loading
    }

    suspend fun handleSignInResult(result: SignInResult) {
        when (result) {
            is SignInResult.Success -> {
                signInUseCase(result.idToken)

                fetchUser()
                _uiEvent.send(AccountUiEvent.SignInSuccess)
            }

            is SignInResult.Cancelled -> {
                _uiState.value = AccountUiState.SignedOut
                _uiEvent.send(AccountUiEvent.SignInCancelled)
            }

            is SignInResult.Error -> {
                _uiState.value = AccountUiState.SignedOut
                _uiEvent.send(AccountUiEvent.SignInError(result.errorType))
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()

            _uiState.value = AccountUiState.SignedOut
            _uiEvent.send(AccountUiEvent.SignOutSuccess)
        }
    }
}