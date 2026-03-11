package com.example.arsad.data.remote.datasource

import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.remote.api.WeatherApiService
import com.example.arsad.data.remote.responses.ForecastResponse
import com.example.arsad.data.remote.responses.WeatherResponse


class WeatherRemoteDataSourceImpl(
    private val api: WeatherApiService
) : BaseRemoteDataSource(), IWeatherRemoteDataSource {

    override suspend fun getCurrentWeather(params: GetWeatherParams): ApiResult<WeatherResponse> =
        safeApiCall {
            api.getCurrentWeather(params.lat, params.lon, params.units, params.lang)
        }

    override suspend fun getForecast(params: GetWeatherParams): ApiResult<ForecastResponse> =
        safeApiCall {
            api.getForecast(params.lat, params.lon, params.units, params.lang)
        }
}