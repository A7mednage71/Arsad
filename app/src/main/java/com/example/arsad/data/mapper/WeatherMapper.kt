package com.example.arsad.data.mapper

import com.example.arsad.data.remote.responses.ForecastResponse
import com.example.arsad.data.remote.responses.WeatherResponse

fun WeatherResponse.applyUnitConversion(tempUnit: String, windUnit: String): WeatherResponse {
    return this.copy(
        weatherMain = this.weatherMain.copy(
            temp = WeatherUnitsConverter.convertTemp(this.weatherMain.temp, tempUnit),
            tempMin = WeatherUnitsConverter.convertTemp(this.weatherMain.tempMin, tempUnit),
            feelsLike = WeatherUnitsConverter.convertTemp(this.weatherMain.feelsLike, tempUnit),
            tempMax = WeatherUnitsConverter.convertTemp(this.weatherMain.tempMax, tempUnit)
        ),
        wind = this.wind.copy(
            speed = WeatherUnitsConverter.convertWindSpeed(this.wind.speed, windUnit)
        )
    )
}

fun ForecastResponse.applyUnitConversion(tempUnit: String, windUnit: String): ForecastResponse {
    return this.copy(
        list = this.list.map { item ->
            item.copy(
                main = item.main.copy(
                    temp = WeatherUnitsConverter.convertTemp(item.main.temp, tempUnit),
                    tempMin = WeatherUnitsConverter.convertTemp(item.main.tempMin, tempUnit),
                    tempMax = WeatherUnitsConverter.convertTemp(item.main.tempMax, tempUnit)
                ),
                wind = item.wind.copy(
                    speed = WeatherUnitsConverter.convertWindSpeed(item.wind.speed, windUnit)
                )
            )
        }
    )
}