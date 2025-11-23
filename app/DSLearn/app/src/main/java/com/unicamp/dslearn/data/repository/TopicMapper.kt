package com.unicamp.dslearn.data.repository

import com.unicamp.dslearn.core.model.TopicModel

import com.unicamp.dslearn.data.datasource.remote.dto.TopicItemResponseDTO

fun TopicItemResponseDTO.toModel() =
    TopicModel(
        name = this.name,
        content = this.contentMarkdown
    )
