package com.example.arsad.presentation.map_picker.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.location.LocationProvider
import com.example.arsad.data.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint

class MapPickerViewModel(
    private val locationProvider: LocationProvider,
    private val settingsManager: SettingsManager
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var selectedLocation by mutableStateOf<GeoPoint?>(null)
        private set

    var locationName by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    fun performSearch() {
        if (searchQuery.isBlank()) return
        viewModelScope.launch {
            isLoading = true
            val currentLang = settingsManager.languageFlow.first()
            val result = locationProvider.getCoordinatesFromName(searchQuery, currentLang)
            Log.d("TAG", "fetchGpsLocation: ---------${currentLang}-----------${result}-")
            if (result is LocationResult.Success) {
                selectedLocation = GeoPoint(result.lat, result.lon)
                locationName = result.name
                searchQuery = result.name
            }
            isLoading = false
        }
    }

    fun onMapTap(lat: Double, lon: Double) {
        selectedLocation = GeoPoint(lat, lon)
        viewModelScope.launch(Dispatchers.IO) {
            val currentLang = settingsManager.languageFlow.first()
            val name = locationProvider.resolveLocationName(lat, lon, currentLang)
            Log.d("TAG", "fetchGpsLocation: ----Lang-----${currentLang}---------Name--${name}-")

            withContext(Dispatchers.Main) {
                locationName = name
                searchQuery = name
            }
        }
    }
}