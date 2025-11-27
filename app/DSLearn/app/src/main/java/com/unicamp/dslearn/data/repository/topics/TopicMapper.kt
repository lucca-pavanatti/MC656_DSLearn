package com.unicamp.dslearn.data.repository.topics

import com.unicamp.dslearn.core.model.TopicModel

import com.unicamp.dslearn.data.datasource.remote.dto.TopicItemResponseDTO
import kotlin.Boolean

fun TopicItemResponseDTO.toModel(
    completed: Boolean = false,
    unlocked: Boolean = false
) =
    TopicModel(
        name = this.name,
        content = this.contentMarkdown,
        completed = completed,
        unlocked = unlocked,
    )
