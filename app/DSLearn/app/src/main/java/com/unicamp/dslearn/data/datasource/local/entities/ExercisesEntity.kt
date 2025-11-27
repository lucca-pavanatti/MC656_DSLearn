package com.unicamp.dslearn.data.datasource.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExercisesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "relatedTopics") val relatedTopics: String,
    @ColumnInfo(name = "companies") val companies: String,
    @ColumnInfo(name = "completed") val completed: Boolean = false
)
