package com.example.arsad.data.remote.responses

import com.example.arsad.data.remote.dto.ForecastCityDto
import com.example.arsad.data.remote.dto.ForecastItemDto
import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("list") val list: List<ForecastItemDto>,
    @SerializedName("city") val city: ForecastCityDto
)
