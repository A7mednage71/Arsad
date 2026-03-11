package com.example.arsad.data.models

data class GetWeatherParams(
    val lat: Double,
    val lon: Double,
    val units: String,
    val lang: String,
    val tempUnit: String,
    val windUnit: String
)