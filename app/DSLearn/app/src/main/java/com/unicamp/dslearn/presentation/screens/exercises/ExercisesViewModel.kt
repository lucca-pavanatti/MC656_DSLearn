package com.unicamp.dslearn.presentation.screens.exercises

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.unicamp.dslearn.core.model.ExerciseModel
import com.unicamp.dslearn.domain.exercises.GetExercisesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ExercisesViewModel(
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    val searchQueryState = TextFieldState()

    private val _difficulty = MutableStateFlow<String?>(null)
    private val _topic = MutableStateFlow<String?>(null)
    private val _company = MutableStateFlow<String?>(null)

    @OptIn(FlowPreview::class)
    val exerciseListState: StateFlow<PagingData<ExerciseModel>> =
        combine(
            snapshotFlow { searchQueryState.text }.debounce(500),
            _difficulty,
            _topic,
            _company
        ) { query, difficulty, topic, company ->
            FilterParams(query.toString(), difficulty, topic, company)
        }.flatMapLatest { params ->
            getExercisesUseCase(
                difficulty = params.difficulty,
                dataStructure = params.topic,
                company = params.company
            ).map { pagingData ->
                pagingData.filter { exercise ->
                    exercise.title.contains(params.query, ignoreCase = true)
                }
            }
        }.cachedIn(viewModelScope)
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                PagingData.empty()
            )

    fun updateFilters(difficulty: String?, topic: String?, company: String?) {
        _difficulty.value = difficulty
        _topic.value = topic
        _company.value = company
    }

    private data class FilterParams(
        val query: String,
        val difficulty: String?,
        val topic: String?,
        val company: String?
    )
}