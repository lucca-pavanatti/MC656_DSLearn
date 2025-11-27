package com.unicamp.dslearn.core.model

data class TopicModel(
    val name: String,
    val content: String,
    val completed: Boolean,
    val unlocked: Boolean,
)

