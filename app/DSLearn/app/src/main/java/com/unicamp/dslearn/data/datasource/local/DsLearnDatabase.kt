package com.unicamp.dslearn.data.datasource.local

import androidx.room.RoomDatabase
import androidx.room.Database
import com.unicamp.dslearn.data.datasource.local.dao.TopicsDao
import com.unicamp.dslearn.data.datasource.local.entities.TopicEntity

@Database(version = 1, entities = [TopicEntity::class], exportSchema = false)
abstract class DsLearnDatabase : RoomDatabase() {
    abstract fun topicsDao(): TopicsDao

    companion object {
        const val DATABASE_NAME = "DsLearn"
    }
}