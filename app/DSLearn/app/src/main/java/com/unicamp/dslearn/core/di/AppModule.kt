package com.unicamp.dslearn.core.di

import org.koin.core.module.dsl.viewModel

import com.unicamp.dslearn.presentation.home.HomeViewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel() }
}