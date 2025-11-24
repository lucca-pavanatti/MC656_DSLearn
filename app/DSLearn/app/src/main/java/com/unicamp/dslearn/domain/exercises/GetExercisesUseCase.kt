package com.unicamp.dslearn.domain.exercises

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.ExercisesModel
import kotlinx.coroutines.flow.Flow

interface GetExercisesUseCase {
    operator fun invoke(difficulty: String, dataStructure: String): Flow<PagingData<ExercisesModel>>
}