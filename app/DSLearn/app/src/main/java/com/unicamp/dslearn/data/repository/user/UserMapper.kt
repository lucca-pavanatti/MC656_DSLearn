package com.unicamp.dslearn.data.repository.user

import com.unicamp.dslearn.core.model.UserMetricsModel
import com.unicamp.dslearn.core.model.UserModel
import com.unicamp.dslearn.data.datasource.remote.dto.UserDTO
import com.unicamp.dslearn.data.datasource.remote.dto.UserMetricsDTO

fun UserDTO.toModel() =
    UserModel(
        id = this.id,
        name = this.name,
        email = this.email
    )

fun UserMetricsDTO.toModel() = UserMetricsModel(
    totalTopics = this.totalTopics,
    completedTopics = this.completedTopics,
    inProgressTopics = this.inProgressTopics,
    completedExercises = this.completedExercises
)
