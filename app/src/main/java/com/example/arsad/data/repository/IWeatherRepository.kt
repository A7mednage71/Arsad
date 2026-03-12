package com.example.arsad.data.repository

import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.data.remote.datasource.ApiResult
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getFullWeatherData(
        params: GetWeatherParams, caching: Boolean
    ): ApiResult<WeatherModel>

    suspend fun fetchAndSaveLocation(lat: Double, lon: Double): Result<Unit>

    fun getAllSavedLocations(): Flow<List<SavedLocationEntity>>

    suspend fun deleteSavedLocation(locationId: Int)

    suspend fun updateSavedLocationById(
        id: Int,
        temp: Double,
        icon: String
    )
}