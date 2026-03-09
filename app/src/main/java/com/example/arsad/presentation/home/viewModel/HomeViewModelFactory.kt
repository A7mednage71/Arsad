package com.example.arsad.presentation.home.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arsad.data.local.SettingsManager
import com.example.arsad.data.repository.IWeatherRepository

class HomeViewModelFactory(
    private val repository: IWeatherRepository,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                repository = repository,
                settingsManager = SettingsManager(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}