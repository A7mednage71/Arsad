package com.example.arsad.data.repository

import com.example.arsad.data.local.datasource.IWeatherLocalDataSource
import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.mapper.toEntity
import com.example.arsad.data.mapper.toUIModel
import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.remote.datasource.IWeatherRemoteDataSource
import com.example.arsad.data.remote.responses.WeatherResponse
import com.example.arsad.util.getCountryNameFromCode
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    private val remoteDataSource: IWeatherRemoteDataSource,
    private val localDataSource: IWeatherLocalDataSource
) : IWeatherRepository {

    override suspend fun getFullWeatherData(
        params: GetWeatherParams,
        caching: Boolean
    ): ApiResult<WeatherModel> {
        return try {
            coroutineScope {
                val weatherDeferred = async { remoteDataSource.getCurrentWeather(params) }
                val forecastDeferred = async { remoteDataSource.getForecast(params) }

                val weatherRes = weatherDeferred.await()
                val forecastRes = forecastDeferred.await()

                if (weatherRes is ApiResult.Success && forecastRes is ApiResult.Success) {
                    val weatherModel = WeatherModel.from(
                        weather = weatherRes.data,
                        forecast = forecastRes.data
                    )
                    // true for home data only
                    if (caching) {
                        localDataSource.saveWeather(weatherModel.toEntity())
                    }

                    ApiResult.Success(weatherModel)
                } else {
                    val errorMessage = when {
                        weatherRes is ApiResult.Failure -> weatherRes.message
                        forecastRes is ApiResult.Failure -> forecastRes.message
                        else -> "Failed to synchronize weather data"
                    }
                    // if local data is available,
                    // return it only in home screen
                    if (caching) fetchFromLocal(errorMessage)
                    else ApiResult.Failure(errorMessage)
                }
            }
        } catch (e: Exception) {
            val msg = e.localizedMessage ?: "An unexpected error occurred"
            if (caching) fetchFromLocal(msg)
            else ApiResult.Failure(msg)
        }
    }

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): ApiResult<WeatherResponse> {
        val params = GetWeatherParams(
            lat = lat, lon = lon,
            units = "metric", lang = lang,
            tempUnit = "C", windUnit = "MS"
        )
        return remoteDataSource.getCurrentWeather(params)
    }

    override suspend fun fetchAndSaveLocation(
        lat: Double,
        lon: Double,
        lang: String
    ): Result<Unit> {
        return try {
            val params = GetWeatherParams(
                lat = lat, lon = lon,
                units = "metric", lang = lang,
                tempUnit = "C", windUnit = "MS"
            )
            when (val result = remoteDataSource.getCurrentWeather(params)) {
                is ApiResult.Success -> {
                    val response = result.data
                    val country = response.sys.country.getCountryNameFromCode(lang)
                    val entity = SavedLocationEntity(
                        cityName = response.name,
                        lat = lat,
                        lon = lon,
                        lastTemp = response.weatherMain.temp,
                        iconCode = response.weather.firstOrNull()?.icon ?: "",
                        country = country,
                        timestamp = System.currentTimeMillis()
                    )
                    localDataSource.insertSavedLocation(entity)
                    Result.success(Unit)
                }

                is ApiResult.Failure -> Result.failure(Exception(result.message))
                else -> Result.failure(Exception("Unknown error occurred"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllSavedLocations(): Flow<List<SavedLocationEntity>> {
        return localDataSource.getAllSavedLocations()
    }

    override suspend fun deleteSavedLocation(locationId: Int) {
        localDataSource.deleteSavedLocation(locationId)
    }

    override suspend fun updateSavedLocationById(id: Int, temp: Double, icon: String) {
        localDataSource.updateSavedLocation(id, temp, icon, System.currentTimeMillis())
    }

    override fun getAllAlerts(): Flow<List<WeatherAlertEntity>> {
        return localDataSource.getAllAlerts()
    }

    override suspend fun insertAlert(alert: WeatherAlertEntity): Long {
        return localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alertId: Int) {
        localDataSource.deleteAlert(alertId)
    }

    override suspend fun updateAlertStatus(alertId: Int, isEnabled: Boolean) {
        localDataSource.updateAlertStatus(alertId, isEnabled)
    }

    override suspend fun getAlertById(alertId: Int): WeatherAlertEntity? {
        return localDataSource.getAlertById(alertId)
    }

    private suspend fun fetchFromLocal(errorMessage: String): ApiResult<WeatherModel> {
        val cachedEntity = localDataSource.getCachedWeather()
        return if (cachedEntity != null) {
            ApiResult.Success(cachedEntity.toUIModel())
        } else {
            ApiResult.Failure(errorMessage)
        }
    }
}