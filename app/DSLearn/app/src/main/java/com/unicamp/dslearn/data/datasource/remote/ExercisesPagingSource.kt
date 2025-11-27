package com.unicamp.dslearn.data.datasource.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.unicamp.dslearn.data.datasource.remote.api.ExercisesApi
import com.unicamp.dslearn.data.datasource.remote.dto.ExerciseItemResponseDTO
import com.unicamp.dslearn.data.fold
import retrofit2.HttpException

class ExercisesPagingSource(
    private val exercisesApi: ExercisesApi,
    private val difficulty: String?,
    private val dataStructure: String?,
    private val company: String?
) : PagingSource<Int, ExerciseItemResponseDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExerciseItemResponseDTO> {
        val page = params.key ?: TOPICS_STARTING_PAGE_INDEX

        return try {
            exercisesApi.getExercises(difficulty ?: "", dataStructure ?: "", company ?: "")
                .fold({ exercisesResponse ->
                exercisesResponse?.let {
                    val results = exercisesResponse.content
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

    override fun getRefreshKey(state: PagingState<Int, ExerciseItemResponseDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val EXERCISES_PAGE_SIZE = 10
        private const val TOPICS_STARTING_PAGE_INDEX = 0
    }

}