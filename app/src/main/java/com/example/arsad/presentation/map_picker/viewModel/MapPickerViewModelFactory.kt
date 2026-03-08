package com.example.arsad.presentation.map_picker.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arsad.data.location.LocationProvider

class MapPickerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapPickerViewModel::class.java)) {
            val locationProvider = LocationProvider(application)
            @Suppress("UNCHECKED_CAST")
            return MapPickerViewModel(locationProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}