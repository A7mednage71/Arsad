package com.example.arsad.presentation.weather_details.viewModel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.repository.IWeatherRepository

class WeatherDetailViewModelFactory(
    private val repository: IWeatherRepository,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherDetailViewModel::class.java)) {
            val settingsManager = SettingsManager(application)
            WeatherDetailViewModel(repository, settingsManager) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}