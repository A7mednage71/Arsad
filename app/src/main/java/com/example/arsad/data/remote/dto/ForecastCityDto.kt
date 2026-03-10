package com.example.arsad.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastCityDto(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String
)

