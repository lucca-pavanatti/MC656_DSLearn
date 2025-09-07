package com.unicamp.dslearn.domain.cardsearch

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.CardModel
import kotlinx.coroutines.flow.Flow

interface SearchCardUseCase {
    operator fun invoke(query: String): Flow<PagingData<CardModel>>
}