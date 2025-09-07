package com.unicamp.dslearn.data.repository

import com.unicamp.dslearn.core.model.CardModel
import com.unicamp.dslearn.core.model.Difficult
import com.unicamp.dslearn.core.model.ExercisesModel
import com.unicamp.dslearn.data.dasource.remote.dto.ExercisesResponseDTO
import com.unicamp.dslearn.data.dasource.remote.dto.SearchCardResponseDTO

fun SearchCardResponseDTO.toModel() =
    CardModel(
        id = this.id,
        name = this.name,
        theory = this.theory,
        exercises = this.exercises.map { it.toModel() },
    )

private fun ExercisesResponseDTO.toModel() = ExercisesModel(
    id = this.id,
    title = this.title,
    difficult = Difficult.valueOf(this.difficult)
)
