package com.example.arsad.presentation.home.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.arsad.R
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.mapper.applyUnitConversion
import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.repository.IWeatherRepository
import com.example.arsad.data.worker.WeatherGlanceSyncWorker
import com.example.arsad.util.NetworkManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    object NoLocation : HomeUiState()
    data class Success(val data: WeatherModel) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val repository: IWeatherRepository,
    private val settingsManager: SettingsManager,
    private val networkManager: NetworkManager,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _showOfflineEvent = MutableSharedFlow<Int>()
    val showOfflineEvent = _showOfflineEvent.asSharedFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private var lastParams: GetWeatherParams? = null

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                settingsManager.latitudeFlow,
                settingsManager.longitudeFlow,
                settingsManager.tempUnitFlow,
                settingsManager.windUnitFlow,
                settingsManager.languageFlow
            ) { lat, lon, temp, wind, lang ->
                if (lat != null && lon != null) {
                    GetWeatherParams(lat, lon, "metric", lang, temp, wind)
                } else null
            }.collect { params ->
                if (params != null) {
                    lastParams = params
                    fetchWeatherData(params)
                } else {
                    // No location saved yet — stop shimmer
                    _uiState.value = HomeUiState.NoLocation
                }
            }
        }
    }

    private fun fetchWeatherData(params: GetWeatherParams, isRefresh: Boolean = false) {
        viewModelScope.launch {
            // Initial State Handling
            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _uiState.value = HomeUiState.Loading
            }

            // Pre-fetch Connectivity Check
            if (!networkManager.hasInternet()) {
                _showOfflineEvent.emit(R.string.snackbar_no_internet)
            }

            // Data Fetching
            when (val result = repository.getFullWeatherData(params, true)) {
                is ApiResult.Success -> {
                    _uiState.value = HomeUiState.Success(
                        data = result.data.applyUnitConversion(
                            targetTempUnit = params.tempUnit,
                            targetWindUnit = params.windUnit
                        )
                    )
                    refreshGlanceWidget(context = context)
                }

                is ApiResult.Failure -> {
                    if (!isRefresh || _uiState.value !is HomeUiState.Success) {
                        _uiState.value = HomeUiState.Error(result.message)
                    } else {
                        // Just notify the user that refresh failed
                        _showOfflineEvent.emit(R.string.snackbar_update_failed)
                    }
                }

                is ApiResult.Loading -> {}
            }

            _isRefreshing.value = false
        }
    }

    fun refresh() {
        lastParams?.let {
            fetchWeatherData(it, isRefresh = true)
        }
    }

    fun refreshGlanceWidget(context: Context) {
        val immediateWork = OneTimeWorkRequestBuilder<WeatherGlanceSyncWorker>()
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "IMMEDIATE_WIDGET_SYNC",
            ExistingWorkPolicy.REPLACE,
            immediateWork
        )
    }
}