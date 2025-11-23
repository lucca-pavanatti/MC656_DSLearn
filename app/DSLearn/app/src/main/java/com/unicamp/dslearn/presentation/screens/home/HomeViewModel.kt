package com.unicamp.dslearn.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.unicamp.dslearn.core.model.TopicModel
import com.unicamp.dslearn.domain.topics.GetTopicsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    getTopicsUseCase: GetTopicsUseCase
) : ViewModel() {

    val topicListState: Flow<PagingData<TopicModel>> =
        getTopicsUseCase("")
            .cachedIn(
                viewModelScope
            )
}