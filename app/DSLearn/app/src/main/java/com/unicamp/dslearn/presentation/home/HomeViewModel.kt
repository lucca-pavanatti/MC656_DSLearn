package com.unicamp.dslearn.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.domain.topics.GetTopicsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    getTopicsUseCase: GetTopicsUseCase
) : ViewModel() {

    val searchQueryState = TextFieldState()

    @OptIn(FlowPreview::class)
    val topicListState: StateFlow<PagingData<TopicModel>> =
        snapshotFlow { searchQueryState.text }
            .debounce(500)
            .flatMapLatest { query ->
                getTopicsUseCase(query.toString()).map { pagingData ->
                    pagingData.filter { topic ->
                        topic.name.contains(query, ignoreCase = true)
                    }
                }
            }.cachedIn(
                viewModelScope
            ).stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                PagingData.empty()
            )

}