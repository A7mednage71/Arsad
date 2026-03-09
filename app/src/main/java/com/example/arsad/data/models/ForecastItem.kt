package com.example.arsad.data.models

import com.google.gson.annotations.SerializedName

data class ForecastItem(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: WeatherMain,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt_txt") val dtTxt: String
)
