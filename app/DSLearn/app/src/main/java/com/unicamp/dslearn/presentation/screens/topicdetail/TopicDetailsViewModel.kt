package com.unicamp.dslearn.presentation.screens.topicdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unicamp.dslearn.domain.topics.GetTopicsUseCase
import com.unicamp.dslearn.domain.topics.SetTopicAsCompletedUseCase
import kotlinx.coroutines.launch

class TopicDetailsViewModel(
    private val setTopicAsCompletedUseCase: SetTopicAsCompletedUseCase
) : ViewModel() {

    fun setTopicAsCompleted(topicName: String) {
        viewModelScope.launch {
            setTopicAsCompletedUseCase(topicName)
        }
    }

}