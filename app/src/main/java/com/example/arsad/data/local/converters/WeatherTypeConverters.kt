package com.example.arsad.data.local.converters

import androidx.room.TypeConverter
import com.example.arsad.data.models.DailyWeatherModel
import com.example.arsad.data.models.HourlyWeatherModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// This class for converting complex data types which Room can't handle to and from JSON
class WeatherTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromHourlyList(list: List<HourlyWeatherModel>?): String =
        gson.toJson(list ?: emptyList<HourlyWeatherModel>())

    @TypeConverter
    fun toHourlyList(value: String?): List<HourlyWeatherModel> {
        val type = object : TypeToken<List<HourlyWeatherModel>>() {}.type
        return gson.fromJson(value ?: "[]", type)
    }

    @TypeConverter
    fun fromDailyList(list: List<DailyWeatherModel>?): String =
        gson.toJson(list ?: emptyList<DailyWeatherModel>())

    @TypeConverter
    fun toDailyList(value: String?): List<DailyWeatherModel> {
        val type = object : TypeToken<List<DailyWeatherModel>>() {}.type
        return gson.fromJson(value ?: "[]", type)
    }
}