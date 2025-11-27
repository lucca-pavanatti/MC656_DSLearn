package com.unicamp.dslearn.domain.exercises

fun interface SetExerciseAsCompletedUseCase {
    suspend operator fun invoke(id: Int)
}