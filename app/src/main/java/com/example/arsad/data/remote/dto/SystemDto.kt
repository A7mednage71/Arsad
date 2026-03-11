package com.example.arsad.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SystemDto(
    @SerializedName("type") val type: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)

