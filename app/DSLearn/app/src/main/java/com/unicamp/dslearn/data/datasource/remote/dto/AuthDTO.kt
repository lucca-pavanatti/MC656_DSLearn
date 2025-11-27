package com.unicamp.dslearn.data.datasource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthDTO(@SerialName("idToken") val idToken: String)


