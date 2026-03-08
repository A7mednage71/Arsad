package com.example.arsad.presentation.settings.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arsad.data.local.SettingsManager
import com.example.arsad.data.location.LocationProvider

class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                settingsManager = SettingsManager(application),
                locationProvider = LocationProvider(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}