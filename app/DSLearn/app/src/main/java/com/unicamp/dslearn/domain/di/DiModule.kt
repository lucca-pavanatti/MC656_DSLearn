package com.unicamp.dslearn.domain.di

import com.unicamp.dslearn.domain.cardsearch.SearchCardUseCase
import com.unicamp.dslearn.domain.cardsearch.SearchCardUseCaseImpl
import org.koin.dsl.module

val diModule = module {
    factory<SearchCardUseCase> { SearchCardUseCaseImpl(get()) }
}