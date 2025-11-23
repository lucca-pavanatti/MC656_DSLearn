package com.unicamp.dslearn.data.repository.exercises

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.unicamp.dslearn.core.model.ExercisesModel
import com.unicamp.dslearn.data.datasource.remote.ExercisesPagingSource
import com.unicamp.dslearn.data.datasource.remote.TopicsPagingSource.Companion.TOPICS_PAGE_SIZE
import com.unicamp.dslearn.data.datasource.remote.api.ExercisesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ExercisesRepositoryImpl(private val exercisesApi: ExercisesApi) :
    ExercisesRepository {

    override fun getExercises(
        difficult: String,
        dataStructure: String
    ): Flow<PagingData<ExercisesModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = TOPICS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ExercisesPagingSource(exercisesApi) },
        ).flow.map { pagingData ->
            pagingData.map {
                it.toModel()
            }
        }
    }
}