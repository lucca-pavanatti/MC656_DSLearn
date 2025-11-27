package com.unicamp.dslearn.core.model

data class UserMetricsModel(
    val totalTopics: Int = 0,
    val completedTopics: Int = 0,
    val topicsCompletionPercentage: Float = 0.0F,
    val inProgressExercises: Int = 0,
    val completedExercises: Int = 0,
    val totalExercises: Int = 0,
    val exercisesCompletionPercentage: Float = 0.0F,
    val easyPercentage: Float = 0.0F,
    val mediumPercentage: Float = 0.0F,
    val hardPercentage: Float = 0.0F,
    val overallProgress: Float = 0.0F,
)