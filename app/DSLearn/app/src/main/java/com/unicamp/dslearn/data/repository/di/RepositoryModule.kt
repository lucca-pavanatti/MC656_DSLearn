package com.unicamp.dslearn.data.repository.di

import com.unicamp.dslearn.data.repository.TopicsRepository
import com.unicamp.dslearn.data.repository.TopicsRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<TopicsRepository> { TopicsRepositoryImpl(get()) }
}