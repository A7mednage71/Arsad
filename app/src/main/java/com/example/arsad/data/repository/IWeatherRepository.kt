package com.example.arsad.data.repository

import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.remote.responses.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getFullWeatherData(
        params: GetWeatherParams, caching: Boolean
    ): ApiResult<WeatherModel>

    suspend fun getWeather(lat: Double, lon: Double, lang: String): ApiResult<WeatherResponse>

    suspend fun fetchAndSaveLocation(lat: Double, lon: Double, lang: String): Result<Unit>

    fun getAllSavedLocations(): Flow<List<SavedLocationEntity>>

    suspend fun deleteSavedLocation(locationId: Int)

    suspend fun updateSavedLocationById(
        id: Int,
        temp: Double,
        icon: String
    )

    fun getAllAlerts(): Flow<List<WeatherAlertEntity>>
    suspend fun insertAlert(alert: WeatherAlertEntity): Long
    suspend fun deleteAlert(alertId: Int)
    suspend fun updateAlertStatus(alertId: Int, isEnabled: Boolean)

    suspend fun getAlertById(alertId: Int): WeatherAlertEntity?
}