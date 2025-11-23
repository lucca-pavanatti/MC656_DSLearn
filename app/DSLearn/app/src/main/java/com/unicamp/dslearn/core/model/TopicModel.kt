package com.unicamp.dslearn.core.model

data class TopicModel(
    val name: String,
    val content: String
)

data class ExercisesModel(
    val id: Int,
    val title: String,
    val difficult: Difficult,
)

enum class Difficult {
    EASY,
    MEDIUM,
    HARD
}

