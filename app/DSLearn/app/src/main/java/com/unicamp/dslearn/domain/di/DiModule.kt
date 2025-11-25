package com.unicamp.dslearn.domain.di

import com.unicamp.dslearn.domain.auth.SignInUseCase
import com.unicamp.dslearn.domain.auth.SignInUseCaseImpl
import com.unicamp.dslearn.domain.auth.SignOutUseCase
import com.unicamp.dslearn.domain.auth.SignOutUseCaseImpl
import com.unicamp.dslearn.domain.exercises.GetExercisesUseCase
import com.unicamp.dslearn.domain.exercises.GetExercisesUseCaseImpl
import com.unicamp.dslearn.domain.exercises.SetExerciseAsCompletedUseCase
import com.unicamp.dslearn.domain.exercises.SetExerciseAsCompletedUseCaseImpl
import com.unicamp.dslearn.domain.topics.GetTopicsUseCase
import com.unicamp.dslearn.domain.topics.GetTopicsUseCaseImpl
import com.unicamp.dslearn.domain.topics.SetTopicAsCompletedUseCase
import com.unicamp.dslearn.domain.topics.SetTopicAsCompletedUseCaseImpl
import com.unicamp.dslearn.domain.user.GetCurrentUserUseCase
import com.unicamp.dslearn.domain.user.GetCurrentUserUseCaseImpl
import com.unicamp.dslearn.domain.user.GetUserMetricsUseCase
import com.unicamp.dslearn.domain.user.GetUserMetricsUseCaseImpl
import org.koin.dsl.module

val diModule = module {
    factory<GetTopicsUseCase> { GetTopicsUseCaseImpl(get()) }

    factory<SetTopicAsCompletedUseCase> { SetTopicAsCompletedUseCaseImpl(get()) }

    factory<GetExercisesUseCase> { GetExercisesUseCaseImpl(get()) }

    factory<SetExerciseAsCompletedUseCase> { SetExerciseAsCompletedUseCaseImpl(get()) }

    factory<SignInUseCase> { SignInUseCaseImpl(get()) }

    factory<SignOutUseCase> { SignOutUseCaseImpl(get()) }

    factory<GetCurrentUserUseCase> { GetCurrentUserUseCaseImpl(get()) }

    factory<GetUserMetricsUseCase> { GetUserMetricsUseCaseImpl(get()) }
}