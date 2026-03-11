package com.example.arsad.data.mapper

import com.example.arsad.data.local.entity.WeatherEntity
import com.example.arsad.data.models.DailyWeatherModel
import com.example.arsad.data.models.HourlyWeatherModel
import com.example.arsad.data.models.WeatherModel

fun WeatherModel.applyUnitConversion(): WeatherModel = this.copy(
    temp = WeatherUnitsConverter.convertTemp(temp, tempUnit),
    feelsLike = WeatherUnitsConverter.convertTemp(feelsLike, tempUnit),
    tempMin = WeatherUnitsConverter.convertTemp(tempMin, tempUnit),
    tempMax = WeatherUnitsConverter.convertTemp(tempMax, tempUnit),
    windSpeed = WeatherUnitsConverter.convertWindSpeed(windSpeed, windUnit),
    hourlyForecast = hourlyForecast.map { it.applyUnitConversion(tempUnit) },
    dailyForecast = dailyForecast.map { it.applyUnitConversion(tempUnit, windUnit) }
)

private fun HourlyWeatherModel.applyUnitConversion(tempUnit: String): HourlyWeatherModel =
    this.copy(
        temp = WeatherUnitsConverter.convertTemp(temp, tempUnit)
    )

private fun DailyWeatherModel.applyUnitConversion(
    tempUnit: String,
    windUnit: String
): DailyWeatherModel = this.copy(
    tempMin = WeatherUnitsConverter.convertTemp(tempMin, tempUnit),
    tempMax = WeatherUnitsConverter.convertTemp(tempMax, tempUnit),
    windSpeed = WeatherUnitsConverter.convertWindSpeed(windSpeed, windUnit)
)


// for local data source (Room)
fun WeatherModel.toEntity(): WeatherEntity = WeatherEntity(
    cityName = cityName, timestamp = timestamp, temp = temp,
    feelsLike = feelsLike, tempMin = tempMin, tempMax = tempMax,
    description = description, iconCode = iconCode, humidity = humidity,
    pressure = pressure, cloudiness = cloudiness, windSpeed = windSpeed,
    tempUnit = tempUnit, windUnit = windUnit,
    hourlyForecast = hourlyForecast, dailyForecast = dailyForecast
)

fun WeatherEntity.toUIModel(): WeatherModel = WeatherModel(
    cityName = cityName, timestamp = timestamp, temp = temp,
    feelsLike = feelsLike, tempMin = tempMin, tempMax = tempMax,
    description = description, iconCode = iconCode, humidity = humidity,
    pressure = pressure, cloudiness = cloudiness, windSpeed = windSpeed,
    tempUnit = tempUnit, windUnit = windUnit,
    hourlyForecast = hourlyForecast, dailyForecast = dailyForecast
)