package com.unicamp.dslearn.presentation.screens.account.metrics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicamp.dslearn.domain.user.GetUserMetricsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MetricsViewModel(private val getUserMetricsUseCase: GetUserMetricsUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<MetricsUiState>(MetricsUiState.Loaded(null))
    val uiState: StateFlow<MetricsUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = MetricsUiState.Loading
        viewModelScope.launch {

            _uiState.value = MetricsUiState.Loaded(
                userMetrics = getUserMetricsUseCase()
            )
        }
    }
}