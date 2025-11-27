package com.unicamp.dslearn.data.datasource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExercisesResponseDTO(
    @SerialName("content") val content: List<ExerciseItemResponseDTO> = emptyList(),
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("totalElements") val totalElements: Int,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("first") val first: Boolean,
    @SerialName("last") val last: Boolean
)

@Serializable
data class ExerciseItemResponseDTO(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String = "",
    @SerialName("url") val url: String = "",
    @SerialName("difficulty") val difficulty: String = "",
    @SerialName("relatedTopics") val relatedTopics: String = "",
    @SerialName("companies") val companies: String = ""
)

@Serializable
data class ExerciseStatusDTO(
    @SerialName("status") val status: String,
    @SerialName("exercise_id") val exerciseId: Int,
)

@Serializable
data class ExerciseProgressResponseDTO(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("status") val status: String,
)