package com.example.arsad.data.local.datasource

import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun saveWeather(weather: WeatherEntity)
    suspend fun getCachedWeather(): WeatherEntity?
    suspend fun clearCache()

    suspend fun insertSavedLocation(location: SavedLocationEntity)
    suspend fun deleteSavedLocation(locationId: Int)
    fun getAllSavedLocations(): Flow<List<SavedLocationEntity>>

    suspend fun updateSavedLocation(id: Int, temp: Double, icon: String, time: Long)

}