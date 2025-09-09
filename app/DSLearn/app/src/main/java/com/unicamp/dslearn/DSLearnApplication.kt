package com.unicamp.dslearn

import android.app.Application
import com.unicamp.dslearn.core.di.appModule
import com.unicamp.dslearn.data.network.networkModule
import com.unicamp.dslearn.data.repository.di.repositoryModule
import com.unicamp.dslearn.domain.di.diModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DSLearnApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DSLearnApplication)
            modules(appModule)
            modules(networkModule)
            modules(diModule)
            modules(repositoryModule)
        }
    }
}