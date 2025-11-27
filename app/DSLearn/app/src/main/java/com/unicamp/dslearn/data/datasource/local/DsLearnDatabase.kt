package com.unicamp.dslearn.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unicamp.dslearn.data.datasource.local.dao.ExercisesDao
import com.unicamp.dslearn.data.datasource.local.dao.TopicsDao
import com.unicamp.dslearn.data.datasource.local.entities.ExercisesEntity
import com.unicamp.dslearn.data.datasource.local.entities.TopicEntity

@Database(
    version = 1,
    entities = [TopicEntity::class, ExercisesEntity::class],
    exportSchema = false
)
abstract class DsLearnDatabase : RoomDatabase() {

    abstract fun topicsDao(): TopicsDao

    abstract fun exercisesDao(): ExercisesDao

    companion object {
        const val DATABASE_NAME = "DsLearn"
    }
}