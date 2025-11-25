package com.unicamp.dslearn.presentation.screens.account.metrics

import com.unicamp.dslearn.core.model.UserMetricsModel

sealed class MetricsUiState {

    data object Loading : MetricsUiState()

    data class Loaded(
        val userMetrics: UserMetricsModel?
    ) : MetricsUiState()
}
