package com.example.arsad.data.local.datasource

import com.example.arsad.data.local.dao.SavedLocationDao
import com.example.arsad.data.local.dao.WeatherAlertDao
import com.example.arsad.data.local.dao.WeatherDao
import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow


class WeatherLocalDataSourceImpl(
    private val dao: WeatherDao,
    private val savedLocationDao: SavedLocationDao,
    private val weatherAlertDao: WeatherAlertDao
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

    override fun getAllAlerts(): Flow<List<WeatherAlertEntity>> {
        return weatherAlertDao.getAllAlerts()
    }

    override suspend fun insertAlert(alert: WeatherAlertEntity) {
        weatherAlertDao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alertId: Int) {
        weatherAlertDao.deleteAlertById(alertId)
    }

    override suspend fun updateAlertStatus(alertId: Int, isEnabled: Boolean) {
        weatherAlertDao.updateAlertStatus(alertId, isEnabled)
    }
}