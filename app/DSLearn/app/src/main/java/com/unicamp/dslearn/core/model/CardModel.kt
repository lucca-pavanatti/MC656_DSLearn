package com.unicamp.dslearn.core.model

data class CardModel(
    val id: Int,
    val name: String,
    val theory: String,
    val exercises: List<ExercisesModel>
)

data class ExercisesModel(
    val id: Int,
    val title: String,
    val difficult: Difficult,
)

enum class Difficult(name: String) {
    Easy("EASY"),
    Medium("MEDIUM"),
    Hard("HARD")
}

