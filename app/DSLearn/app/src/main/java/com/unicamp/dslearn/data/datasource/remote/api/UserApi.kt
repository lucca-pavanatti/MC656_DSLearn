package com.unicamp.dslearn.data.datasource.remote.api

import com.unicamp.dslearn.data.datasource.remote.dto.TopicsProgressResponseDTO
import com.unicamp.dslearn.data.datasource.remote.dto.TopicsResponseDTO
import com.unicamp.dslearn.data.datasource.remote.dto.TopicsStatusDTO
import com.unicamp.dslearn.data.datasource.remote.dto.UserDTO
import com.unicamp.dslearn.data.datasource.remote.dto.UserMetricsDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("/users/info")
    suspend fun getCurrentUser(
        @Header("Token") token: String
    ): Response<UserDTO>

    @GET("/users/{id}/metrics")
    suspend fun getUserMetrics(
        @Path("id") id: Int
    ): Response<UserMetricsDTO>

    @PUT("/users/{userId}/topics/status")
    suspend fun updateTopic(
        @Path("userId") userId: Int,
        @Body topicStatus: TopicsStatusDTO
    ): Response<Unit>
}