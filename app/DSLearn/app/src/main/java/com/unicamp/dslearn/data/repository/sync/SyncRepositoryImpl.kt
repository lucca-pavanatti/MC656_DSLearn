package com.unicamp.dslearn.data.repository.sync

import com.unicamp.dslearn.data.datasource.local.dao.ExercisesDao
import com.unicamp.dslearn.data.datasource.local.dao.TopicsDao
import com.unicamp.dslearn.data.repository.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class SyncRepositoryImpl(
    private val topicsDao: TopicsDao,
    private val exercisesDao: ExercisesDao,
    private val userRepository: UserRepository,
) : SyncRepository {
    override suspend fun uploadUserProgress() {
        withContext(Dispatchers.IO) {
            val topicList = topicsDao.getAll().first()
            val exerciseList = exercisesDao.getAll().first()

            val topicUploads = topicList
                .filter { it.completed }
                .map { topic ->
                    async {
                        userRepository.setTopicAsCompleted(topic.name)
                    }
                }

            val exerciseUploads = exerciseList
                .filter { it.completed }
                .map { exercise ->
                    async {
                        userRepository.setExerciseAsCompleted(exercise.id)
                    }
                }

            topicUploads.awaitAll()
            exerciseUploads.awaitAll()
        }
    }
}
