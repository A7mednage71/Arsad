package com.example.arsad.data.remote.responses

import com.example.arsad.data.models.Clouds
import com.example.arsad.data.models.Coordinates
import com.example.arsad.data.models.System
import com.example.arsad.data.models.Weather
import com.example.arsad.data.models.WeatherMain
import com.example.arsad.data.models.Wind
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord") val coordinates: Coordinates,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val weatherMain: WeatherMain,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt") val dt: Long,
    @SerializedName("sys") val sys: System,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Int
)
