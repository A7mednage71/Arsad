package com.example.arsad.data.local.datasource

import com.example.arsad.data.local.dao.WeatherDao
import com.example.arsad.data.local.entity.WeatherEntity


class WeatherLocalDataSourceImpl(
    private val dao: WeatherDao
) : IWeatherLocalDataSource {

    override suspend fun saveWeather(weather: WeatherEntity) {
        dao.insertWeather(weather)
    }

    override suspend fun getCachedWeather(): WeatherEntity? {
        return dao.getCachedWeather()
    }

    override suspend fun clearCache() {
        dao.clearCache()
    }
}