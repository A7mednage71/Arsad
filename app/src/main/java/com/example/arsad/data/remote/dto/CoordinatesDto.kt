package com.example.arsad.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CoordinatesDto(
    @SerializedName("lon") val lon: Double,
    @SerializedName("lat") val lat: Double
)

