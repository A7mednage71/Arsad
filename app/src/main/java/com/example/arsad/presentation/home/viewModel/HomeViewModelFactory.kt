package com.example.arsad.presentation.home.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.repository.IWeatherRepository
import com.example.arsad.util.NetworkManager

class HomeViewModelFactory(
    private val repository: IWeatherRepository,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val networkManager = NetworkManager(application)
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                repository = repository,
                settingsManager = SettingsManager(application),
                networkManager = networkManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}