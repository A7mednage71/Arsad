package com.example.arsad.data.models

import com.google.gson.annotations.SerializedName

data class ForecastCity(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String
)
