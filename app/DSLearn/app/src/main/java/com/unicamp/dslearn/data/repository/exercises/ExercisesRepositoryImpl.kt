package com.unicamp.dslearn.data.repository.exercises

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.unicamp.dslearn.core.model.ExerciseModel
import com.unicamp.dslearn.data.datasource.local.dao.ExercisesDao
import com.unicamp.dslearn.data.datasource.local.entities.ExercisesEntity
import com.unicamp.dslearn.data.datasource.remote.ExercisesPagingSource
import com.unicamp.dslearn.data.datasource.remote.ExercisesPagingSource.Companion.EXERCISES_PAGE_SIZE
import com.unicamp.dslearn.data.datasource.remote.api.ExercisesApi
import com.unicamp.dslearn.data.fold
import com.unicamp.dslearn.data.repository.user.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class ExercisesRepositoryImpl(
    private val exercisesApi: ExercisesApi,
    private val exercisesDao: ExercisesDao,
    private val userRepository: UserRepository
) : ExercisesRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getExercises(
        difficult: String?,
        dataStructure: String?,
        company: String?
    ): Flow<PagingData<ExerciseModel>> {
        return userRepository.getCurrentUser()
            .flatMapLatest { user ->
                if (user == null) {
                    exercisesDao.getAll().flatMapLatest { localExercises ->
                        createPagingFlow(
                            difficult,
                            dataStructure,
                            company,
                            localExercises,
                            emptyList()
                        )
                    }
                } else {
                    flow {
                        val completedExercises = exercisesApi.getCompletedExercises(user.id).fold(
                            onSuccess = { list ->
                                list?.filter { it.status == "COMPLETED" }
                                    ?.map { it.id }
                                    ?: emptyList()
                            },
                            onFailure = { emptyList() }
                        )
                        emit(completedExercises)
                    }.flatMapLatest { completedExercises ->
                        exercisesDao.getAll().flatMapLatest { localExercises ->
                            createPagingFlow(
                                difficult,
                                dataStructure,
                                company,
                                localExercises,
                                completedExercises
                            )
                        }
                    }
                }
            }

    }

    private fun createPagingFlow(
        difficult: String?,
        dataStructure: String?,
        company: String?,
        localExercises: List<ExercisesEntity>,
        completedExercises: List<Int>
    ): Flow<PagingData<ExerciseModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = EXERCISES_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ExercisesPagingSource(
                    exercisesApi,
                    difficult,
                    dataStructure,
                    company
                )
            },
        ).flow.map { pagingData ->
            val completedList = localExercises.filter {
                it.completed || it.id in completedExercises
            }.associateBy { it.id }

            pagingData.map { exercise ->
                with(exercise) {
                    val exerciseEntity = ExercisesEntity(
                        id = id,
                        title = title,
                        url = url,
                        difficulty = difficulty,
                        relatedTopics = relatedTopics,
                        companies = companies,
                        completed = completedList[id] != null
                    )

                    exercisesDao.insertAll(exerciseEntity)
                    toModel(exerciseEntity.completed)
                }
            }
        }
    }

    override suspend fun setExerciseAsCompleted(id: Int) {
        exercisesDao.updateCompleted(id, true)
        userRepository.setExerciseAsCompleted(id)
    }
}