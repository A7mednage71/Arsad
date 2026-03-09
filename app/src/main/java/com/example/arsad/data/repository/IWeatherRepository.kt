package com.example.arsad.data.repository

import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.remote.responses.ForecastResponse
import com.example.arsad.data.remote.responses.WeatherResponse

interface IWeatherRepository {
    suspend fun getCurrentWeather(params: GetWeatherParams): Result<WeatherResponse>
    suspend fun getForecast(params: GetWeatherParams): Result<ForecastResponse>
}