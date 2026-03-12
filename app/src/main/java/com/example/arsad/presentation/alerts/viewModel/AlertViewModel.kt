package com.example.arsad.presentation.alerts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.mapper.toUIModel
import com.example.arsad.data.models.WeatherAlertModel
import com.example.arsad.data.repository.IWeatherRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlertViewModel(
    private val repository: IWeatherRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    val alerts: StateFlow<List<WeatherAlertModel>> = repository.getAllAlerts()
        .map { entities ->
            entities.map { it.toUIModel() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveAlert(startTime: Long, endTime: Long, alertType: String) {
        viewModelScope.launch {
            val lat = settingsManager.latitudeFlow.first() ?: 0.0
            val lon = settingsManager.longitudeFlow.first() ?: 0.0
            val locationName = settingsManager.locationNameFlow.first() ?: ""

            val newAlertEntity = WeatherAlertEntity(
                lat = lat,
                lon = lon,
                locationName = locationName,
                startTime = startTime,
                endTime = endTime,
                alertType = alertType,
                isEnabled = true
            )

            repository.insertAlert(newAlertEntity)
        }
    }


    fun deleteAlert(alertId: Int) {
        viewModelScope.launch {
            repository.deleteAlert(alertId)
        }
    }


    fun toggleAlertStatus(alertId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateAlertStatus(alertId, isEnabled)
        }
    }
}