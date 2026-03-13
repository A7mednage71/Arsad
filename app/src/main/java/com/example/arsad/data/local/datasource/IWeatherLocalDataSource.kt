package com.example.arsad.data.local.datasource

import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    // home screen
    suspend fun saveWeather(weather: WeatherEntity)
    suspend fun getCachedWeather(): WeatherEntity?
    suspend fun clearCache()

    // saved screen
    suspend fun insertSavedLocation(location: SavedLocationEntity)
    suspend fun deleteSavedLocation(locationId: Int)
    fun getAllSavedLocations(): Flow<List<SavedLocationEntity>>
    suspend fun updateSavedLocation(id: Int, temp: Double, icon: String, time: Long)

    // alert screen
    fun getAllAlerts(): Flow<List<WeatherAlertEntity>>
    suspend fun insertAlert(alert: WeatherAlertEntity): Long
    suspend fun deleteAlert(alertId: Int)
    suspend fun updateAlertStatus(alertId: Int, isEnabled: Boolean)

    suspend fun getAlertById(alertId: Int): WeatherAlertEntity?

}