package com.example.arsad.presentation.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsad.data.local.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


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


data class SettingsUiState(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.METER_PER_SEC,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val locationMethod: LocationMethod = LocationMethod.GPS
)


class SettingsViewModel(private val settingsManager: SettingsManager) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                settingsManager.languageFlow,
                settingsManager.tempUnitFlow,
                settingsManager.windUnitFlow,
                settingsManager.locationMethodFlow
            ) { lang, tempUnit, windUnit, locationMethod ->
                SettingsUiState(
                    language = if (lang == "ar") AppLanguage.ARABIC else AppLanguage.ENGLISH,
                    temperatureUnit = when (tempUnit) {
                        "F" -> TemperatureUnit.FAHRENHEIT
                        "K" -> TemperatureUnit.KELVIN
                        else -> TemperatureUnit.CELSIUS
                    },
                    windSpeedUnit = if (windUnit == "MPH") WindSpeedUnit.MILE_PER_HOUR else WindSpeedUnit.METER_PER_SEC,
                    locationMethod = if (locationMethod == "MAP") LocationMethod.MAP else LocationMethod.GPS
                )
            }.collect { loadedState ->
                _uiState.value = loadedState
            }
        }
    }


    fun setTemperatureUnit(unit: TemperatureUnit) {
        val code = when (unit) {
            TemperatureUnit.CELSIUS -> "C"
            TemperatureUnit.FAHRENHEIT -> "F"
            TemperatureUnit.KELVIN -> "K"
        }
        viewModelScope.launch { settingsManager.saveTempUnit(code) }
    }

    fun setWindSpeedUnit(unit: WindSpeedUnit) {
        val code = if (unit == WindSpeedUnit.MILE_PER_HOUR) "MPH" else "MS"
        viewModelScope.launch { settingsManager.saveWindUnit(code) }
    }

    suspend fun setLanguage(language: AppLanguage) {
        settingsManager.saveLanguage(language.code)
    }

    fun setLocationMethod(method: LocationMethod) {
        val code = if (method == LocationMethod.MAP) "MAP" else "GPS"
        viewModelScope.launch { settingsManager.saveLocationMethod(code) }
    }
}