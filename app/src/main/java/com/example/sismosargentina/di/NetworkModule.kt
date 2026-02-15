package com.example.sismosargentina.di

import com.example.sismosargentina.data.remote.EarthquakeApiService
import com.example.sismosargentina.data.repository.EarthquakeRepository
import com.example.sismosargentina.domain.usecase.GetLatestEarthquakesUseCase
import com.example.sismosargentina.presentation.viewmodel.EarthquakeViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/"
private const val TIMEOUT_SECONDS = 30L

val appModule = module {
    // Moshi
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // OkHttpClient
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    // API Service
    single {
        get<Retrofit>().create(EarthquakeApiService::class.java)
    }

    // Repository
    single { EarthquakeRepository(get()) }

    // Use Case
    factory { GetLatestEarthquakesUseCase(get()) }

    // ViewModel
    viewModel { EarthquakeViewModel(get()) }
}
