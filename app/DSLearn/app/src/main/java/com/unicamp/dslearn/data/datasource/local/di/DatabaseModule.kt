package com.unicamp.dslearn.data.datasource.local.di

import android.content.Context
import androidx.room.Room
import com.unicamp.dslearn.data.datasource.local.DsLearnDatabase
import com.unicamp.dslearn.data.datasource.local.dao.TopicsDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private object DatabaseModule {

    fun provideDatabase(applicationContext: Context): DsLearnDatabase {
        return Room.databaseBuilder(
            applicationContext,
            DsLearnDatabase::class.java,
            DsLearnDatabase.DATABASE_NAME,
        ).allowMainThreadQueries().build()
    }

    fun providesTopicsDao(
        database: DsLearnDatabase,
    ): TopicsDao = database.topicsDao()
}

val databaseModule = module {
    single<DsLearnDatabase> { DatabaseModule.provideDatabase(androidContext()) }
    single { DatabaseModule.providesTopicsDao(get()) }
}