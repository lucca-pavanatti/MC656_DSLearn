package com.unicamp.dslearn.core.di

import com.unicamp.dslearn.presentation.screens.account.AccountViewModel
import com.unicamp.dslearn.presentation.screens.exercises.ExercisesViewModel
import com.unicamp.dslearn.presentation.screens.home.HomeViewModel
import com.unicamp.dslearn.presentation.screens.topicdetail.TopicDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ExercisesViewModel(get()) }
    viewModel { AccountViewModel(get(), get(), get(), get()) }
    viewModel { TopicDetailsViewModel(get()) }
}