package com.unicamp.dslearn.data.dasource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDTO(
    @SerialName("resultCount") val resultCount: Int = 0,
    @SerialName("results") val results: List<SearchCardResponseDTO> = emptyList(),
)

@Serializable
data class SearchCardResponseDTO(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("theory") val theory: String = "",
    @SerialName("exercises") val exercises: List<ExercisesResponseDTO> = emptyList()
)

@Serializable
data class ExercisesResponseDTO(
    @SerialName("id") val id: Int = 0,
    @SerialName("title") val title: String = "",
    @SerialName("difficult") val difficult: String = ""
)
