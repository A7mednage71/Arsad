package com.example.arsad.data.remote.responses

import com.example.arsad.data.models.ForecastCity
import com.example.arsad.data.models.ForecastItem
import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("list") val list: List<ForecastItem>,
    @SerializedName("city") val city: ForecastCity
)

