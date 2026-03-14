package com.example.arsad.presentation.alerts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.mapper.toUIModel
import com.example.arsad.data.models.WeatherAlertModel
import com.example.arsad.data.repository.IWeatherRepository
import com.example.arsad.data.worker.WeatherWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AlertViewModel(
    private val repository: IWeatherRepository,
    private val settingsManager: SettingsManager,
    private val workManager: WorkManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val alerts: StateFlow<List<WeatherAlertModel>> = combine(
        repository.getAllAlerts(), settingsManager.languageFlow
    ) { entities, lang ->
        val uiList = entities.map { it.toUIModel(lang) }
        _isLoading.value = false
        uiList
    }.stateIn(
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

            val id = repository.insertAlert(newAlertEntity)
            scheduleAlert(id.toInt(), startTime, alertType)
        }
    }

    fun deleteAlert(alertId: Int) {
        viewModelScope.launch {
            repository.deleteAlert(alertId)
            cancelAlert(alertId)
        }
    }

    fun toggleAlertStatus(alertId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateAlertStatus(alertId, isEnabled)

            if (isEnabled) {
                val alert = repository.getAlertById(alertId)

                alert?.let {
                    val currentTime = System.currentTimeMillis()
                    when {
                        // The alert time has not yet come
                        it.startTime > currentTime -> {
                            scheduleAlert(it.id, it.startTime, it.alertType)
                        }
                        // We are currently in the warning period
                        currentTime in it.startTime..it.endTime -> {
                            //send the current time as start time
                            scheduleAlert(it.id, currentTime, it.alertType)
                        }
                        // alert has completely expired
                        else -> {
                            repository.updateAlertStatus(alertId, false)
                        }
                    }
                }
            } else {
                cancelAlert(alertId)
            }
        }
    }


    private fun scheduleAlert(alertId: Int, startTime: Long, alertType: String) {
        val delay = startTime - System.currentTimeMillis()

        if (delay >= 0) {
            val data = workDataOf(
                "ALERT_ID" to alertId,
                "ALERT_TYPE" to alertType
            )

            val workRequest = OneTimeWorkRequestBuilder<WeatherWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("weather_alert_$alertId")
                .build()

            workManager.enqueueUniqueWork(
                "work_$alertId",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }

    private fun cancelAlert(alertId: Int) {
        workManager.cancelUniqueWork("work_$alertId")
    }
}