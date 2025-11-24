package com.unicamp.dslearn.domain.exercises

import com.unicamp.dslearn.data.repository.exercises.ExercisesRepository

internal class GetExercisesUseCaseImpl(private val exercisesRepository: ExercisesRepository) :
    GetExercisesUseCase {

    override fun invoke(difficulty: String, dataStructure: String) =
        exercisesRepository.getExercises(difficulty, dataStructure)
}