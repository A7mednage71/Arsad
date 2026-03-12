package com.example.arsad.presentation.saved.viewModel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.repository.IWeatherRepository

class SavedViewModelFactory(
    private val application: Application,
    private val repository: IWeatherRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SavedViewModel::class.java)) {
            SavedViewModel(
                repository, settingsManager = SettingsManager(application),
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}