package com.unicamp.dslearn.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unicamp.dslearn.data.datasource.local.entities.ExercisesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercisesDao {
    @Query("SELECT * FROM ExercisesEntity")
    fun getAll(): Flow<List<ExercisesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg topic: ExercisesEntity)

    @Query("UPDATE ExercisesEntity SET completed = :completed WHERE id = :id")
    suspend fun updateCompleted(id: Int, completed: Boolean)
}