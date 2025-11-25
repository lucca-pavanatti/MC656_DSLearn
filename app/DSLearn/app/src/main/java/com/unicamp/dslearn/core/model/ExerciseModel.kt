package com.unicamp.dslearn.core.model

data class ExerciseModel(
    val id: Int,
    val title: String,
    val url: String,
    val difficulty: Difficulty,
    val relatedTopics: String,
    val companies: String,
    val completed: Boolean
)

enum class Difficulty() {
    EASY,
    MEDIUM,
    HARD,
    NA;

    companion object {
        fun fromString(value: String): Difficulty {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: NA
        }
    }
}