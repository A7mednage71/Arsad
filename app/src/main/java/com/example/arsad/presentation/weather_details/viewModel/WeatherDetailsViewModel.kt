package com.example.arsad.presentation.weather_details.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.mapper.applyUnitConversion
import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.repository.IWeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class WeatherDetailState {
    object Loading : WeatherDetailState()
    data class Success(val data: WeatherModel) : WeatherDetailState()
    data class Error(val message: String) : WeatherDetailState()
}

class WeatherDetailViewModel(
    private val repository: IWeatherRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _weatherDetailState =
        MutableStateFlow<WeatherDetailState>(WeatherDetailState.Loading)
    val weatherDetailState = _weatherDetailState.asStateFlow()

    fun fetchWeatherDetails(lat: Double, lon: Double, id: Int) {
        viewModelScope.launch {

            _weatherDetailState.value = WeatherDetailState.Loading

            val tempUnit = settingsManager.tempUnitFlow.first()
            val windUnit = settingsManager.windUnitFlow.first()
            val lang = settingsManager.languageFlow.first()

            val params = GetWeatherParams(
                lat = lat,
                lon = lon,
                units = "metric",
                lang = lang,
                tempUnit = tempUnit,
                windUnit = windUnit
            )

            // No caching
            val result = repository.getFullWeatherData(params, caching = false)

            _weatherDetailState.value = when (result) {
                is ApiResult.Success -> {
                    repository.updateSavedLocationById(
                        id = id,
                        temp = result.data.temp,
                        icon = result.data.iconCode
                    )
                    WeatherDetailState.Success(
                        data = result.data.applyUnitConversion(
                            targetTempUnit = tempUnit,
                            targetWindUnit = windUnit
                        )
                    )
                }

                is ApiResult.Failure -> WeatherDetailState.Error(result.message)
                else -> WeatherDetailState.Error("Unknown Error occurred")
            }
        }
    }
}