package com.example.arsad.data.remote.api

import com.example.arsad.data.remote.network.RetrofitHelper
import com.example.arsad.data.remote.responses.ForecastResponse
import com.example.arsad.data.remote.responses.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") apiKey: String = RetrofitHelper.API_KEY
    ): Response<WeatherResponse>

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("cnt") count: Int = 40,
        @Query("appid") apiKey: String = RetrofitHelper.API_KEY
    ): Response<ForecastResponse>
}

