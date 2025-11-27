package com.unicamp.dslearn.data.datasource.remote.api

import com.unicamp.dslearn.data.datasource.remote.dto.AuthDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth")
    suspend fun auth(@Body album: AuthDTO): Response<Unit>

}