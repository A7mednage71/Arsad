package com.example.arsad.di

import androidx.work.WorkManager
import com.example.arsad.data.local.datasource.IWeatherLocalDataSource
import com.example.arsad.data.local.datasource.WeatherLocalDataSourceImpl
import com.example.arsad.data.local.db.WeatherDatabase
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.remote.api.WeatherApiService
import com.example.arsad.data.remote.datasource.IWeatherRemoteDataSource
import com.example.arsad.data.remote.datasource.WeatherRemoteDataSourceImpl
import com.example.arsad.data.repository.IWeatherRepository
import com.example.arsad.data.repository.WeatherRepositoryImpl
import com.example.arsad.data.worker.WeatherWorker
import com.example.arsad.presentation.alerts.viewModel.AlertViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // 1. Database & DAOs
    single { WeatherDatabase.getInstance(get()) }
    single { get<WeatherDatabase>().weatherDao() }
    single { get<WeatherDatabase>().savedLocationDao() }
    single { get<WeatherDatabase>().weatherAlertDao() }

    // 2. Network (Retrofit)
    single {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    // 3. Settings & Manager
    single { SettingsManager(get()) }
    single { WorkManager.getInstance(get()) }

    // 4. DataSources
    single<IWeatherRemoteDataSource> { WeatherRemoteDataSourceImpl(get()) }
    single<IWeatherLocalDataSource> {
        WeatherLocalDataSourceImpl(
            get(), // Context
            savedLocationDao = get(),
            weatherAlertDao = get()
        )
    }

    // 5. Repository
    single<IWeatherRepository> { WeatherRepositoryImpl(get(), get()) }

    // 6. ViewModel
    viewModel { AlertViewModel(get(), get(), get()) }

    // 7. Worker
    worker { WeatherWorker(get(), get(), get()) }
}