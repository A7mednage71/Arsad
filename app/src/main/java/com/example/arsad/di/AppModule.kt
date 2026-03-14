package com.example.arsad.di

import androidx.work.WorkManager
import com.example.arsad.data.local.datasource.IWeatherLocalDataSource
import com.example.arsad.data.local.datasource.WeatherLocalDataSourceImpl
import com.example.arsad.data.local.db.WeatherDatabase
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.location.LocationProvider
import com.example.arsad.data.remote.api.ApiConstants
import com.example.arsad.data.remote.api.WeatherApiService
import com.example.arsad.data.remote.datasource.IWeatherRemoteDataSource
import com.example.arsad.data.remote.datasource.WeatherRemoteDataSourceImpl
import com.example.arsad.data.repository.IWeatherRepository
import com.example.arsad.data.repository.WeatherRepositoryImpl
import com.example.arsad.data.worker.WeatherWorker
import com.example.arsad.presentation.alerts.viewModel.AlertViewModel
import com.example.arsad.presentation.home.viewModel.HomeViewModel
import com.example.arsad.presentation.map_picker.viewModel.MapPickerViewModel
import com.example.arsad.presentation.saved.viewModel.SavedViewModel
import com.example.arsad.presentation.settings.viewModel.SettingsViewModel
import com.example.arsad.presentation.weather_details.viewModel.WeatherDetailViewModel
import com.example.arsad.util.NetworkManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    // Database & DAOs
    single { WeatherDatabase.getInstance(androidContext()) }
    single { get<WeatherDatabase>().weatherDao() }
    single { get<WeatherDatabase>().savedLocationDao() }
    single { get<WeatherDatabase>().weatherAlertDao() }

    // Network (Retrofit + OkHttp for stability)

    single {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("appid", ApiConstants.API_KEY)
                    .build()
                val requestBuilder = original.newBuilder().url(url)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    // Managers & Providers
    single { SettingsManager(androidContext()) }
    single { LocationProvider(androidContext()) }
    single { WorkManager.getInstance(androidContext()) }
    single { NetworkManager(androidContext()) }

    // DataSources
    single<IWeatherRemoteDataSource> { WeatherRemoteDataSourceImpl(get()) }
    single<IWeatherLocalDataSource> {
        WeatherLocalDataSourceImpl(
            savedLocationDao = get(),
            dao = get(),
            weatherAlertDao = get()
        )
    }

    // Repository
    single<IWeatherRepository> { WeatherRepositoryImpl(get(), get()) }

    //  ViewModels
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { AlertViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { SavedViewModel(get(), get()) }
    viewModel { WeatherDetailViewModel(get(), get()) }
    viewModel { MapPickerViewModel(get(), get()) }

    //  Worker (Koin WorkManager support)
    worker { WeatherWorker(get(), get(), get(), get()) }
}