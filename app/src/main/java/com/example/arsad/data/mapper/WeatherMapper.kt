package com.example.arsad.data.mapper

import com.example.arsad.data.local.entity.WeatherEntity
import com.example.arsad.data.models.DailyWeatherModel
import com.example.arsad.data.models.HourlyWeatherModel
import com.example.arsad.data.models.WeatherModel

// targetTempUnit / targetWindUnit: override the unit stored in the model.
// Always pass the current user-selected units so cached data (stored as raw "C"/"MS")
// is correctly converted to whatever the user has selected right now.
fun WeatherModel.applyUnitConversion(
    targetTempUnit: String = tempUnit,
    targetWindUnit: String = windUnit
): WeatherModel = this.copy(
    temp = WeatherUnitsConverter.convertTemp(temp, targetTempUnit),
    feelsLike = WeatherUnitsConverter.convertTemp(feelsLike, targetTempUnit),
    tempMin = WeatherUnitsConverter.convertTemp(tempMin, targetTempUnit),
    tempMax = WeatherUnitsConverter.convertTemp(tempMax, targetTempUnit),
    windSpeed = WeatherUnitsConverter.convertWindSpeed(windSpeed, targetWindUnit),
    tempUnit = targetTempUnit,
    windUnit = targetWindUnit,
    hourlyForecast = hourlyForecast.map { it.applyUnitConversion(targetTempUnit) },
    dailyForecast = dailyForecast.map { it.applyUnitConversion(targetTempUnit, targetWindUnit) }
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
// Always store raw metric units (C + MS) so applyUnitConversion() always works
// correctly regardless of when the cache was written or what unit the user had at that time.
fun WeatherModel.toEntity(): WeatherEntity = WeatherEntity(
    cityName = cityName, timestamp = timestamp, temp = temp,
    feelsLike = feelsLike, tempMin = tempMin, tempMax = tempMax,
    description = description, iconCode = iconCode, humidity = humidity,
    pressure = pressure, cloudiness = cloudiness, windSpeed = windSpeed,
    tempUnit = "C", windUnit = "MS",
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