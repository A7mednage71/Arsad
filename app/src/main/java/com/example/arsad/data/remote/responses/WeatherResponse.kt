package com.example.arsad.data.remote.responses

import com.example.arsad.data.remote.dto.CloudsDto
import com.example.arsad.data.remote.dto.CoordinatesDto
import com.example.arsad.data.remote.dto.SystemDto
import com.example.arsad.data.remote.dto.WeatherDto
import com.example.arsad.data.remote.dto.WeatherMainDto
import com.example.arsad.data.remote.dto.WindDto
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord") val coordinates: CoordinatesDto,
    @SerializedName("weather") val weather: List<WeatherDto>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val weatherMain: WeatherMainDto,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("clouds") val clouds: CloudsDto,
    @SerializedName("dt") val dt: Long,
    @SerializedName("sys") val sys: SystemDto,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Int
)
