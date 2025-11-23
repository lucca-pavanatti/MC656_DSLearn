package com.unicamp.dslearn.data.repository.exercises

import com.unicamp.dslearn.core.model.Difficulty
import com.unicamp.dslearn.core.model.ExercisesModel
import com.unicamp.dslearn.data.datasource.remote.dto.ExerciseItemResponseDTO

fun ExerciseItemResponseDTO.toModel() =
    ExercisesModel(
        id = this.id,
        title = this.title,
        url = this.url,
        difficulty = Difficulty.fromString(this.difficulty),
        relatedTopics = this.relatedTopics,
        companies = this.companies,
    )
