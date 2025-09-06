package com.unicamp.dslearn.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.unicamp.dslearn.core.model.CardModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel : ViewModel() {

    val searchQueryState = TextFieldState()

    @OptIn(FlowPreview::class)
    val cardListState: StateFlow<PagingData<CardModel>> =
        snapshotFlow { searchQueryState.text }
            .debounce(500)
            .flatMapLatest { query ->
                if (query.isNotBlank()) {
//                searchCardUseCase(query)
//                    .distinctUntilChanged()
                    flow { emit(PagingData.empty<CardModel>()) }
                } else {
                    flow { emit(PagingData.empty<CardModel>()) }
                }
            }.cachedIn(
                viewModelScope
            ).stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                PagingData.empty()
            )
}