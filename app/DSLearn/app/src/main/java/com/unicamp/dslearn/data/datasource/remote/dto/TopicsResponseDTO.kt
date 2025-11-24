package com.unicamp.dslearn.data.datasource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponseDTO(
    @SerialName("content") val content: List<TopicItemResponseDTO> = emptyList(),
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("totalElements") val totalElements: Int,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("first") val first: Boolean,
    @SerialName("last") val last: Boolean
)


@Serializable
data class TopicItemResponseDTO(
    @SerialName("name") val name: String = "",
    @SerialName("contentMarkdown") val contentMarkdown: String = "",
)

@Serializable
data class TopicsStatusDTO(
    @SerialName("status") val status: String,
    @SerialName("topic_name") val topicName: String,
)

@Serializable
data class TopicsProgressResponseDTO(
    @SerialName("name") val name: String,
    @SerialName("status") val status: String,
)

