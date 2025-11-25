package com.unicamp.dslearn.domain.exercises

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.ExerciseModel
import com.unicamp.dslearn.data.repository.exercises.ExercisesRepository
import kotlinx.coroutines.flow.Flow

internal class GetExercisesUseCaseImpl(private val exercisesRepository: ExercisesRepository) :
    GetExercisesUseCase {

    override fun invoke(
        difficulty: String?,
        dataStructure: String?,
        company: String?
    ): Flow<PagingData<ExerciseModel>> =
        exercisesRepository.getExercises(difficulty, dataStructure, company)
}