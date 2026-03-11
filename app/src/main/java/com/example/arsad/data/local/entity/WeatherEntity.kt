package com.example.arsad.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.arsad.data.models.DailyWeatherModel
import com.example.arsad.data.models.HourlyWeatherModel

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey val id: Int = 0, // only one row in the table
    val cityName: String,
    val timestamp: Long,
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val description: String,
    val iconCode: String,
    val humidity: Int,
    val pressure: Int,
    val cloudiness: Int,
    val windSpeed: Double,
    val tempUnit: String,
    val windUnit: String,
    // these lists are converted to JSON using Gson
    val hourlyForecast: List<HourlyWeatherModel>,
    val dailyForecast: List<DailyWeatherModel>
)