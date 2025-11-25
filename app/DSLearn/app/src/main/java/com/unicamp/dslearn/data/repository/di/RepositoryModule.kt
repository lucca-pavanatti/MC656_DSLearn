package com.unicamp.dslearn.data.repository.di

import com.unicamp.dslearn.data.repository.auth.AuthRepository
import com.unicamp.dslearn.data.repository.auth.AuthRepositoryImpl
import com.unicamp.dslearn.data.repository.auth.TokenManager
import com.unicamp.dslearn.data.repository.exercises.ExercisesRepository
import com.unicamp.dslearn.data.repository.exercises.ExercisesRepositoryImpl
import com.unicamp.dslearn.data.repository.sync.SyncRepository
import com.unicamp.dslearn.data.repository.sync.SyncRepositoryImpl
import com.unicamp.dslearn.data.repository.topics.TopicsRepository
import com.unicamp.dslearn.data.repository.topics.TopicsRepositoryImpl
import com.unicamp.dslearn.data.repository.user.UserRepository
import com.unicamp.dslearn.data.repository.user.UserRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { TokenManager(androidContext()) }

    single<TopicsRepository> { TopicsRepositoryImpl(get(), get(), get()) }

    single<ExercisesRepository> { ExercisesRepositoryImpl(get(), get(), get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single<SyncRepository> { SyncRepositoryImpl(get(), get(), get()) }
}