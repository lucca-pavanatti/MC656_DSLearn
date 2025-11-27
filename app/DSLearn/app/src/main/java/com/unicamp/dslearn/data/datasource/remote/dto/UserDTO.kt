package com.unicamp.dslearn.data.datasource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
)