package com.example.arsad.presentation.settings.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsad.R
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.location.LocationProvider
import com.example.arsad.data.location.LocationResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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

enum class AppTheme(val label: String) {
    LIGHT("Light"),
    DARK("Dark")
}

enum class LocationMethod(val label: String) {
    GPS("GPS"),
    MAP("Map")
}


data class SettingsUiState(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.METER_PER_SEC,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val theme: AppTheme = AppTheme.LIGHT,
    val locationMethod: LocationMethod = LocationMethod.GPS,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationName: String? = null
)


class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val toastMessage: SharedFlow<Int> = _toastMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            combine<Any?, SettingsUiState>(
                settingsManager.languageFlow,
                settingsManager.tempUnitFlow,
                settingsManager.windUnitFlow,
                settingsManager.locationMethodFlow,
                settingsManager.latitudeFlow,
                settingsManager.longitudeFlow,
                settingsManager.locationNameFlow,
                settingsManager.isDarkModeFlow
            ) { args ->
                val lang = args[0] as String
                val tempUnit = args[1] as String
                val windUnit = args[2] as String
                val locationMethod = args[3] as String
                val lat = args[4] as? Double
                val lon = args[5] as? Double
                val locName = args[6] as? String
                val isDark = args[7] as Boolean

                SettingsUiState(
                    language = if (lang == "ar") AppLanguage.ARABIC else AppLanguage.ENGLISH,
                    temperatureUnit = when (tempUnit) {
                        "F" -> TemperatureUnit.FAHRENHEIT
                        "K" -> TemperatureUnit.KELVIN
                        else -> TemperatureUnit.CELSIUS
                    },
                    windSpeedUnit = if (windUnit == "MPH") WindSpeedUnit.MILE_PER_HOUR else WindSpeedUnit.METER_PER_SEC,
                    locationMethod = if (locationMethod == "MAP") LocationMethod.MAP else LocationMethod.GPS,
                    theme = if (isDark) AppTheme.DARK else AppTheme.LIGHT,
                    latitude = lat,
                    longitude = lon,
                    locationName = locName
                )
            }.collect { newState ->
                _uiState.value = newState
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


    fun setTheme(theme: AppTheme) {
        val isDark = theme == AppTheme.DARK
        viewModelScope.launch { settingsManager.saveTheme(isDark) }
    }

    fun setLocationMethod(method: LocationMethod) {
        val code = if (method == LocationMethod.MAP) "MAP" else "GPS"
        viewModelScope.launch { settingsManager.saveLocationMethod(code) }
    }

    fun fetchGpsLocation() {
        viewModelScope.launch {
            val lang = settingsManager.languageFlow.first()
            Log.d("TAG", "fetchGpsLocation: ---------${lang}---")
            val result = locationProvider.getCurrentLocation(lang)
            Log.d("TAG", "fetchGpsLocation: ---------${result}---")
            if (result is LocationResult.Success && result.name != "Unknown Location") {
                saveLocation(result.lat, result.lon, result.name)
                _toastMessage.emit(R.string.toast_location_saved_success)
            } else {
                _toastMessage.emit(R.string.toast_location_failed)
            }
        }
    }

    fun saveLocation(lat: Double, lon: Double, name: String) {
        viewModelScope.launch { settingsManager.saveLocation(lat, lon, name) }
    }
}