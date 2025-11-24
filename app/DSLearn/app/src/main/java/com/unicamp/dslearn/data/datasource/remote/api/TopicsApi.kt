package com.unicamp.dslearn.data.datasource.remote.api

import com.unicamp.dslearn.data.datasource.remote.dto.TopicsProgressResponseDTO
import com.unicamp.dslearn.data.datasource.remote.dto.TopicsResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TopicsApi {

    @GET("/api/topics")
    suspend fun getTopics(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): Response<TopicsResponseDTO>

    @GET("/api/topics/{name}")
    suspend fun getTopicsByName(
        @Path("name") name: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): Response<TopicsResponseDTO>


    @GET("/users/{userId}/topics/progress")
    suspend fun getCompletedTopics(
        @Path("userId") userId: Int,
    ): Response<List<TopicsProgressResponseDTO>>


}