package com.example.arsad.presentation.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsad.data.local.SettingsManager
import com.example.arsad.data.mapper.applyUnitConversion
import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.data.repository.IWeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: WeatherModel) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val repository: IWeatherRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var lastParams: GetWeatherParams? = null

    init {
        viewModelScope.launch {
            combine(
                settingsManager.latitudeFlow,
                settingsManager.longitudeFlow,
                settingsManager.tempUnitFlow,
                settingsManager.windUnitFlow,
                settingsManager.languageFlow
            ) { lat, lon, temp, wind, lang ->
                if (lat != null && lon != null) {
                    GetWeatherParams(lat, lon, "standard", lang, temp, wind)
                } else null
            }.collect { params ->
                params?.let {
                    lastParams = it
                    fetchWeatherData(it)
                }
            }
        }
    }

    private fun fetchWeatherData(params: GetWeatherParams) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                //  Parallel API calls using async/await
                val weatherDeferred = async { repository.getCurrentWeather(params) }
                val forecastDeferred = async { repository.getForecast(params) }

                // Waiting for both results
                val weatherResult = weatherDeferred.await()
                val forecastResult = forecastDeferred.await()

                if (weatherResult.isSuccess && forecastResult.isSuccess) {
                    val raw = WeatherModel.from(
                        weather = weatherResult.getOrThrow(),
                        forecast = forecastResult.getOrThrow(),
                        tempUnit = params.tempUnit,
                        windUnit = params.windUnit
                    )
                    _uiState.value = HomeUiState.Success(
                        data = raw.applyUnitConversion()
                    )
                } else {
                    _uiState.value = HomeUiState.Error("Failed to fetch weather data from server")
                }
            } catch (e: Exception) {
                // English error message for unexpected exceptions
                _uiState.value = HomeUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun refresh() {
        lastParams?.let { fetchWeatherData(it) }
    }
}