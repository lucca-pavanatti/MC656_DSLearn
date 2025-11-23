package com.unicamp.dslearn.data.network

import com.unicamp.dslearn.data.datasource.remote.api.TopicsApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private object NetworkModule {

    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }


    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .build()
    }

    fun provideService(retrofit: Retrofit): TopicsApi =
        retrofit.create(TopicsApi::class.java)

    private const val BASE_URL = "http://10.0.2.2:8080"
}

val networkModule = module {
    single { NetworkModule.provideHttpLoggingInterceptor() }
    single { NetworkModule.provideHttpClient(get()) }
    single { NetworkModule.provideRetrofit(get()) }
    single { NetworkModule.provideService(get()) }
}