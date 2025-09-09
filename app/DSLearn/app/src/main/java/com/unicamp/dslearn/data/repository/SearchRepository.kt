package com.unicamp.dslearn.data.repository

import androidx.paging.PagingData
import com.unicamp.dslearn.core.model.CardModel
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchByQuery(query: String): Flow<PagingData<CardModel>>

}