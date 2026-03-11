package com.example.arsad.data.remote.datasource

import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.remote.responses.ForecastResponse
import com.example.arsad.data.remote.responses.WeatherResponse

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(params: GetWeatherParams): ApiResult<WeatherResponse>
    suspend fun getForecast(params: GetWeatherParams): ApiResult<ForecastResponse>
}