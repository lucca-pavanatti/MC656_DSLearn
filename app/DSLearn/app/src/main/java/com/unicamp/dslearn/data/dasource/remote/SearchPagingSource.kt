package com.unicamp.dslearn.data.dasource.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unicamp.dslearn.data.dasource.remote.api.SearchApi
import com.unicamp.dslearn.data.dasource.remote.dto.SearchCardResponseDTO
import com.unicamp.dslearn.data.fold
import retrofit2.HttpException

class SearchPagingSource(
    private val searchApi: SearchApi,
    private val query: String
) : PagingSource<Int, SearchCardResponseDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchCardResponseDTO> {
        val offset = params.key ?: SEARCH_STARTING_PAGE_INDEX

        return try {
            searchApi.searchByQuery(query = query, limit = params.loadSize, offset = offset)
                .fold({ searchResponse ->
                    searchResponse?.let {
                        val results = searchResponse.results.map {
                            it.copy(name = "${it.name} $offset")
                        }
                        val nextKey = if (results.isEmpty()) {
                            null
                        } else {
                            offset + params.loadSize
                        }
                        LoadResult.Page(
                            data = results,
                            prevKey = if (offset == SEARCH_STARTING_PAGE_INDEX) null else offset - 1,
                            nextKey = nextKey
                        )
                    } ?: LoadResult.Error(Exception())
                }, {
                    LoadResult.Error(Exception())
                })
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchCardResponseDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val SEARCH_PAGE_SIZE = 2
        private const val SEARCH_STARTING_PAGE_INDEX = 0
    }

}