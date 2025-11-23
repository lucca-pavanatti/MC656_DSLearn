package com.unicamp.dslearn.domain.di

import com.unicamp.dslearn.domain.topics.GetTopicsUseCase
import com.unicamp.dslearn.domain.topics.GetTopicsUseCaseImpl
import org.koin.dsl.module

val diModule = module {
    factory<GetTopicsUseCase> { GetTopicsUseCaseImpl(get()) }
}