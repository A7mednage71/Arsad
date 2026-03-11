package com.example.arsad.data.repository

import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.data.remote.datasource.ApiResult

interface IWeatherRepository {
    suspend fun getFullWeatherData(params: GetWeatherParams): ApiResult<WeatherModel>
}