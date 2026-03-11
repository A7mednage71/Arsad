package com.example.arsad.data.local.datasource

import com.example.arsad.data.local.entity.WeatherEntity

interface IWeatherLocalDataSource {
    suspend fun saveWeather(weather: WeatherEntity)
    suspend fun getCachedWeather(): WeatherEntity?
    suspend fun clearCache()
}