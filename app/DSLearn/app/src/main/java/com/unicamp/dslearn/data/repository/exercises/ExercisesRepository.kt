package com.unicamp.dslearn.data.repository.exercises

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.ExercisesModel
import kotlinx.coroutines.flow.Flow

fun interface ExercisesRepository {
    fun getExercises(
        difficult: String,
        dataStructure: String
    ): Flow<PagingData<ExercisesModel>>
}
