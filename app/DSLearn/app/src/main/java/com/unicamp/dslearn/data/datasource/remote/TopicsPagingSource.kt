package com.unicamp.dslearn.data.datasource.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unicamp.dslearn.data.datasource.remote.api.TopicsApi
import com.unicamp.dslearn.data.datasource.remote.dto.TopicItemResponseDTO
import com.unicamp.dslearn.data.fold
import retrofit2.HttpException

class TopicsPagingSource(
    private val topicsApi: TopicsApi,
    private val name: String
) : PagingSource<Int, TopicItemResponseDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicItemResponseDTO> {
        val page = params.key ?: TOPICS_STARTING_PAGE_INDEX

        return try {
            val topicsResponse = if (name.isEmpty()) {
                topicsApi.getTopics(page = page, size = params.loadSize)
            } else {
                topicsApi.getTopicsByName(name = name, page = page, size = params.loadSize)
            }

            topicsResponse.fold({ topicsResponse ->
                topicsResponse?.let {
                    val results = topicsResponse.content
                    val prevKey = if (page > 0) {
                        page - 1
                    } else {
                        null
                    }
                    val nextKey = if (results.isNotEmpty()) {
                        page + 1
                    } else {
                        null
                    }

                    LoadResult.Page(data = results, prevKey = prevKey, nextKey = nextKey)
                } ?: LoadResult.Error(Exception())
            }, {
                LoadResult.Error(Exception())
            })
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TopicItemResponseDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val TOPICS_PAGE_SIZE = 10
        private const val TOPICS_STARTING_PAGE_INDEX = 0
    }

}