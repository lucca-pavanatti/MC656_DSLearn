package com.unicamp.dslearn.domain.di

import com.unicamp.dslearn.domain.topics.GetTopicsUseCase
import com.unicamp.dslearn.domain.topics.GetTopicsUseCaseImpl
import com.unicamp.dslearn.domain.topics.SetTopicAsCompletedUseCase
import com.unicamp.dslearn.domain.topics.SetTopicAsCompletedUseCaseImpl
import org.koin.dsl.module

val diModule = module {
    factory<GetTopicsUseCase> { GetTopicsUseCaseImpl(get()) }
    factory<SetTopicAsCompletedUseCase> { SetTopicAsCompletedUseCaseImpl(get()) }
}