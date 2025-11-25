package com.unicamp.dslearn.data.repository.sync

interface SyncRepository {
    suspend fun uploadUserProgress()
}