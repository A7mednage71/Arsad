package com.example.arsad.data.repository

import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.remote.api.WeatherApiService
import com.example.arsad.data.remote.responses.ForecastResponse
import com.example.arsad.data.remote.responses.WeatherResponse


class WeatherRepositoryImpl(
    private val api: WeatherApiService
) : BaseRepository(), IWeatherRepository {

    override suspend fun getCurrentWeather(params: GetWeatherParams): Result<WeatherResponse> =
        safeApiCall {
            api.getCurrentWeather(params.lat, params.lon, params.units, params.lang)
        }

    override suspend fun getForecast(params: GetWeatherParams): Result<ForecastResponse> =
        safeApiCall {
            api.getForecast(params.lat, params.lon, params.units, params.lang)
        }
}