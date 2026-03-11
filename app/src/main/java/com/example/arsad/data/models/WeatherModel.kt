package com.example.arsad.data.models

import com.example.arsad.data.remote.dto.ForecastItemDto
import com.example.arsad.data.remote.responses.ForecastResponse
import com.example.arsad.data.remote.responses.WeatherResponse


data class WeatherModel(
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
    val hourlyForecast: List<HourlyWeatherModel>,
    val dailyForecast: List<DailyWeatherModel>
) {
    companion object {
        // API always returns metric → values are always raw Celsius + m/s
        fun from(
            weather: WeatherResponse,
            forecast: ForecastResponse
        ): WeatherModel = WeatherModel(
            cityName = weather.name,
            timestamp = weather.dt,
            temp = weather.weatherMain.temp,
            feelsLike = weather.weatherMain.feelsLike,
            tempMin = weather.weatherMain.tempMin,
            tempMax = weather.weatherMain.tempMax,
            description = weather.weather.firstOrNull()?.description
                ?.replaceFirstChar { it.uppercase() } ?: "",
            iconCode = weather.weather.firstOrNull()?.icon ?: "01d",
            humidity = weather.weatherMain.humidity,
            pressure = weather.weatherMain.pressure,
            cloudiness = weather.clouds.all,
            windSpeed = weather.wind.speed,
            tempUnit = "C",  // raw — always Celsius from API (units=metric)
            windUnit = "MS", // raw — always m/s from API (units=metric)
            hourlyForecast = forecast.list.take(8).map { HourlyWeatherModel.from(it) },
            dailyForecast = forecast.list
                .filter { it.dtTxt.contains("12:00:00") }
                .map { DailyWeatherModel.from(it) }
        )
    }
}


data class HourlyWeatherModel(
    val dtTxt: String,
    val temp: Double,
    val iconCode: String
) {
    companion object {
        fun from(dto: ForecastItemDto) = HourlyWeatherModel(
            dtTxt = dto.dtTxt,
            temp = dto.main.temp,
            iconCode = dto.weather.firstOrNull()?.icon ?: "01d"
        )
    }
}


data class DailyWeatherModel(
    val dt: Long,
    val tempMin: Double,
    val tempMax: Double,
    val iconCode: String,
    val description: String,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double
) {
    companion object {
        fun from(dto: ForecastItemDto) = DailyWeatherModel(
            dt = dto.dt,
            tempMin = dto.main.tempMin,
            tempMax = dto.main.tempMax,
            iconCode = dto.weather.firstOrNull()?.icon ?: "01d",
            description = dto.weather.firstOrNull()?.description
                ?.replaceFirstChar { it.uppercase() } ?: "",
            humidity = dto.main.humidity,
            pressure = dto.main.pressure,
            windSpeed = dto.wind.speed
        )
    }
}
