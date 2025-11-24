package com.unicamp.dslearn.presentation.screens.exercises

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.unicamp.dslearn.core.model.ExercisesModel
import com.unicamp.dslearn.domain.exercises.GetExercisesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ExercisesViewModel(
    getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    val searchQueryState = TextFieldState()

    @OptIn(FlowPreview::class)
    val exerciseListState: StateFlow<PagingData<ExercisesModel>> =
        snapshotFlow { searchQueryState.text }
            .debounce(500)
            .flatMapLatest { query ->
                getExercisesUseCase("", "").map { pagingData ->
                    pagingData.filter { exercises ->
                        exercises.title.contains(query, ignoreCase = true)
                    }
                }
            }.cachedIn(
                viewModelScope
            ).stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                PagingData.Companion.empty()
            )

}