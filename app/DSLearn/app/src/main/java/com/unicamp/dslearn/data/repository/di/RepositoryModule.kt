package com.unicamp.dslearn.data.repository.di

import com.unicamp.dslearn.data.repository.SearchRepository
import com.unicamp.dslearn.data.repository.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get()) }
}