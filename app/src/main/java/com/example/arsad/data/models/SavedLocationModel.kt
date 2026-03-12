package com.example.arsad.data.models


data class SavedLocationModel(
    val id: Int,
    val cityName: String,
    val lat: Double,
    val lon: Double,
    val lastTemp: Double,
    val tempUnit: String = "C",
    val iconCode: String,
    val country: String,
    val timestamp: Long
)