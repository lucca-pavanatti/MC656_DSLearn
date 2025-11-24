package com.unicamp.dslearn.data.datasource.remote.api

import com.unicamp.dslearn.data.datasource.remote.dto.UserDTO
import com.unicamp.dslearn.data.datasource.remote.dto.UserMetricsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserApi {
    @GET("/users/info")
    suspend fun getCurrentUser(
        @Header("Token") token: String
    ): Response<UserDTO>

    @GET("/users/{id}/info")
    suspend fun getUserMetrics(
        @Path("id") id: Int
    ): Response<UserMetricsDTO>

}