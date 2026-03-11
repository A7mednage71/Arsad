package com.example.arsad.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastItemDto(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: WeatherMainDto,
    @SerializedName("weather") val weather: List<WeatherDto>,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("clouds") val clouds: CloudsDto,
    @SerializedName("dt_txt") val dtTxt: String
)

