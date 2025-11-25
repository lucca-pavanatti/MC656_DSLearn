package com.unicamp.dslearn.domain.exercises

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.ExerciseModel
import kotlinx.coroutines.flow.Flow

interface GetExercisesUseCase {
    operator fun invoke(
        difficulty: String?,
        dataStructure: String?,
        company: String?
    ): Flow<PagingData<ExerciseModel>>
}