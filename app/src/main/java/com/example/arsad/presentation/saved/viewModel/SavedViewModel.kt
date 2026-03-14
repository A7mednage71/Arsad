package com.example.arsad.presentation.saved.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.mapper.WeatherUnitsConverter
import com.example.arsad.data.mapper.toUIModel
import com.example.arsad.data.models.Coordinates
import com.example.arsad.data.models.SavedLocationModel
import com.example.arsad.data.repository.IWeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SavedViewModel(
    private val weatherRepository: IWeatherRepository, private val settingsManager: SettingsManager,
) : ViewModel() {

    private val _savedLocations = MutableStateFlow<List<SavedLocationModel>>(emptyList())
    val savedLocations = _savedLocations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    init {
        observeSavedLocations()
    }

    private fun observeSavedLocations() {
        viewModelScope.launch {
            _isLoading.value = true
            combine(
                weatherRepository.getAllSavedLocations(),
                settingsManager.tempUnitFlow,
            ) { entities, unit ->
                Log.d("SavedViewModel", "Combine triggered! Mapping data...")
                entities.map { entity ->
                    val model = entity.toUIModel()
                    val convertedTemp = WeatherUnitsConverter.convertTemp(
                        celsius = entity.lastTemp,
                        unit = unit
                    )
                    model.copy(
                        lastTemp = convertedTemp,
                        tempUnit = unit
                    )
                }
            }.collect { mappedList ->
                _savedLocations.value = mappedList
                delay(500)
                _isLoading.value = false
            }
        }
    }


    fun addSavedLocation(location: Coordinates) {
        viewModelScope.launch {
            val lang = settingsManager.languageFlow.first()
            weatherRepository.fetchAndSaveLocation(location.lat, location.lon, lang)
        }
    }

    fun deleteSavedLocation(modelId: Int) {
        viewModelScope.launch {
            weatherRepository.deleteSavedLocation(modelId)
        }
    }
}