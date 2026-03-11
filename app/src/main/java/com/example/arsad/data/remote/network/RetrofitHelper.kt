package com.example.arsad.data.remote.network

import com.example.arsad.data.remote.api.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "6e283989ece956a0861078bbb0810fd8"


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}