package com.unicamp.dslearn.presentation.screens.exercisedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicamp.dslearn.domain.exercises.SetExerciseAsCompletedUseCase

import kotlinx.coroutines.launch

class ExerciseDetailsViewModel(
    private val setExerciseAsCompletedUseCase: SetExerciseAsCompletedUseCase
) : ViewModel() {

    fun setExerciseAsCompleted(exerciseId: Int) {
        viewModelScope.launch {
            setExerciseAsCompletedUseCase(exerciseId)
        }
    }

}