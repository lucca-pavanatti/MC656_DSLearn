package com.unicamp.dslearn.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unicamp.dslearn.data.datasource.local.entities.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicsDao {

    @Query("SELECT * FROM TopicEntity")
    fun getAll(): Flow<List<TopicEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg topic: TopicEntity)

    @Query("UPDATE TopicEntity SET completed = :completed WHERE name = :name")
    suspend fun updateCompleted(name: String, completed: Boolean)
}