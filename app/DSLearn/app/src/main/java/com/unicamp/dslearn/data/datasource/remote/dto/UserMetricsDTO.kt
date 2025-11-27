package com.unicamp.dslearn.data.datasource.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMetricsDTO(
    @SerialName("totalTopics") val totalTopics: Int,
    @SerialName("completedTopics") val completedTopics: Int,
    @SerialName("inProgressTopics") val inProgressTopics: Int,
    @SerialName("notStartedTopics") val notStartedTopics: Int,
    @SerialName("topicsCompletionPercentage") val topicsCompletionPercentage: Float,
    @SerialName("totalExercises") val totalExercises: Int,
    @SerialName("completedExercises") val completedExercises: Int,
    @SerialName("inProgressExercises") val inProgressExercises: Int,
    @SerialName("notStartedExercises") val notStartedExercises: Int,
    @SerialName("exercisesCompletionPercentage") val exercisesCompletionPercentage: Float,
    @SerialName("exercisesByDifficulty")
    val exercisesByDifficulty: ExercisesByDifficultyDTO,
    @SerialName("exercisesByTopic")
    val exercisesByTopic: List<ExercisesByTopicDTO> = emptyList(),
    @SerialName("overallProgress") val overallProgress: Float
)

@Serializable
data class ExercisesByDifficultyDTO(
    @SerialName("Easy") val easy: DifficultyStatsDTO,
    @SerialName("Medium") val medium: DifficultyStatsDTO,
    @SerialName("Hard") val hard: DifficultyStatsDTO
)

@Serializable
data class DifficultyStatsDTO(
    @SerialName("total") val total: Int,
    @SerialName("completed") val completed: Int,
    @SerialName("inProgress") val inProgress: Int,
    @SerialName("notStarted") val notStarted: Int,
    @SerialName("completionPercentage") val completionPercentage: Float
)

@Serializable
data class ExercisesByTopicDTO(
    @SerialName("topicName") val topicName: String,
    @SerialName("totalExercises") val totalExercises: Int,
    @SerialName("completedExercises") val completedExercises: Int,
    @SerialName("inProgressExercises") val inProgressExercises: Int,
    @SerialName("notStartedExercises") val notStartedExercises: Int,
    @SerialName("completionPercentage") val completionPercentage: Float
)
