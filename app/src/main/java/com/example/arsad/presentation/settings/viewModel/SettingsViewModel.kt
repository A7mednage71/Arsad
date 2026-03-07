package com.example.arsad.presentation.settings.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


enum class TemperatureUnit(val label: String, val symbol: String) {
    CELSIUS("Celsius", "°C"),
    FAHRENHEIT("Fahrenheit", "°F"),
    KELVIN("Kelvin", "K")
}

enum class WindSpeedUnit(val label: String, val symbol: String) {
    METER_PER_SEC("Meter/sec", "m/s"),
    MILE_PER_HOUR("Mile/hour", "mph")
}

enum class AppLanguage(val label: String, val code: String) {
    ENGLISH("English", "en"),
    ARABIC("العربية", "ar")
}

enum class LocationMethod(val label: String) {
    GPS("GPS"),
    MAP("Map")
}

// UI state holder

data class SettingsUiState(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.METER_PER_SEC,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val locationMethod: LocationMethod = LocationMethod.GPS
)

class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setTemperatureUnit(unit: TemperatureUnit) {
        _uiState.update { it.copy(temperatureUnit = unit) }
    }

    fun setWindSpeedUnit(unit: WindSpeedUnit) {
        _uiState.update { it.copy(windSpeedUnit = unit) }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(language = language) }
    }

    fun setLocationMethod(method: LocationMethod) {
        _uiState.update { it.copy(locationMethod = method) }
    }
}

