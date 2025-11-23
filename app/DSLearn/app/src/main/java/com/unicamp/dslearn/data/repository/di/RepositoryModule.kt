package com.unicamp.dslearn.data.repository.di

import com.unicamp.dslearn.data.repository.exercises.ExercisesRepository
import com.unicamp.dslearn.data.repository.exercises.ExercisesRepositoryImpl
import com.unicamp.dslearn.data.repository.topics.TopicsRepository
import com.unicamp.dslearn.data.repository.topics.TopicsRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<TopicsRepository> { TopicsRepositoryImpl(get(), get()) }
    single<ExercisesRepository> { ExercisesRepositoryImpl(get()) }
}