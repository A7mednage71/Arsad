package com.example.arsad.data.local.datasource

import com.example.arsad.data.local.dao.SavedLocationDao
import com.example.arsad.data.local.dao.WeatherDao
import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow


class WeatherLocalDataSourceImpl(
    private val dao: WeatherDao,
    private val savedLocationDao: SavedLocationDao
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

    override suspend fun insertSavedLocation(location: SavedLocationEntity) {
        savedLocationDao.insertSavedLocation(location)
    }

    override suspend fun deleteSavedLocation(locationId: Int) {
        savedLocationDao.deleteSavedLocation(locationId)
    }

    override fun getAllSavedLocations(): Flow<List<SavedLocationEntity>> {
        return savedLocationDao.getAllSavedLocations()
    }

    override suspend fun updateSavedLocation(id: Int, temp: Double, icon: String, time: Long) {
        savedLocationDao.updateLocationWeather(id, temp, icon, time)
    }
}