package com.unicamp.dslearn.domain.exercises

import com.unicamp.dslearn.data.repository.exercises.ExercisesRepository

class SetExerciseAsCompletedUseCaseImpl(private val exercisesRepository: ExercisesRepository) :
    SetExerciseAsCompletedUseCase {
    override suspend fun invoke(id: Int) {
        exercisesRepository.setExerciseAsCompleted(id)
    }
}