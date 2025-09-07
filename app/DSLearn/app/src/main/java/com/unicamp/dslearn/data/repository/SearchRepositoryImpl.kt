package com.unicamp.dslearn.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.unicamp.dslearn.core.model.CardModel
import com.unicamp.dslearn.data.datasource.remote.SearchPagingSource
import com.unicamp.dslearn.data.datasource.remote.SearchPagingSource.Companion.SEARCH_PAGE_SIZE
import com.unicamp.dslearn.data.datasource.remote.api.SearchApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SearchRepositoryImpl(private val searchApi: SearchApi) : SearchRepository {

    override fun searchByQuery(query: String): Flow<PagingData<CardModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = SEARCH_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(searchApi, query) },
        ).flow.map { pagingData ->
            pagingData.map {
                it.toModel()
            }
        }
    }
}