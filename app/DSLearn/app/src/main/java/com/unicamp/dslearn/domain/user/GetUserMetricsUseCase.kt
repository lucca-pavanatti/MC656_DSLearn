package com.unicamp.dslearn.domain.user

import com.unicamp.dslearn.core.model.UserMetricsModel

fun interface GetUserMetricsUseCase {
    suspend operator fun invoke(): UserMetricsModel?
}