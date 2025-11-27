package com.unicamp.dslearn.data.repository.exercises

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.ExerciseModel
import kotlinx.coroutines.flow.Flow

interface ExercisesRepository {
    fun getExercises(
        difficult: String?,
        dataStructure: String?,
        company: String?
    ): Flow<PagingData<ExerciseModel>>

    suspend fun setExerciseAsCompleted(id: Int)
}
