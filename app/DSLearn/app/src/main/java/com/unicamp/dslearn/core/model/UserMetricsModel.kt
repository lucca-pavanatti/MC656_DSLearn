package com.unicamp.dslearn.core.model

data class UserMetricsModel(
    val totalTopics: Int,
    val completedTopics: Int,
    val inProgressTopics: Int,
    val completedExercises: Int,
)