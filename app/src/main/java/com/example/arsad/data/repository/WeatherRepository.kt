package com.example.arsad.data.repository

import com.example.arsad.data.local.datasource.IWeatherLocalDataSource
import com.example.arsad.data.mapper.toEntity
import com.example.arsad.data.mapper.toUIModel
import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.remote.datasource.IWeatherRemoteDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class WeatherRepositoryImpl(
    private val remoteDataSource: IWeatherRemoteDataSource,
    private val localDataSource: IWeatherLocalDataSource
) : IWeatherRepository {

    override suspend fun getFullWeatherData(params: GetWeatherParams): ApiResult<WeatherModel> {
        return try {
            coroutineScope {
                // Execute network calls concurrently to optimize performance and reduce latency
                val weatherDeferred = async { remoteDataSource.getCurrentWeather(params) }
                val forecastDeferred = async { remoteDataSource.getForecast(params) }

                val weatherRes = weatherDeferred.await()
                val forecastRes = forecastDeferred.await()

                if (weatherRes is ApiResult.Success && forecastRes is ApiResult.Success) {

                    val weatherModel = WeatherModel.from(
                        weather = weatherRes.data,
                        forecast = forecastRes.data,
                        tempUnit = params.units,
                        windUnit = "m/s"
                    )

                    // Cache the latest weather snapshot in Room
                    localDataSource.saveWeather(weatherModel.toEntity())

                    ApiResult.Success(weatherModel)
                } else {
                    val errorMessage = when {
                        weatherRes is ApiResult.Failure -> weatherRes.message
                        forecastRes is ApiResult.Failure -> forecastRes.message
                        else -> "Failed to synchronize weather data"
                    }
                    fetchFromLocal(errorMessage)
                }
            }
        } catch (e: Exception) {
            fetchFromLocal(e.localizedMessage ?: "An unexpected error occurred")
        }
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