package com.unicamp.dslearn.data.datasource.remote.api

import com.unicamp.dslearn.data.datasource.remote.dto.ExercisesResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExercisesApi {

    @GET("/api/exercises")
    suspend fun getExercises(
        @Query("difficulty") difficulty: String,
        @Query("dataStructure") dataStructure: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): Response<ExercisesResponseDTO>

}