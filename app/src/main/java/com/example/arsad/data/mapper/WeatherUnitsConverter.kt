package com.example.arsad.data.mapper

import kotlin.math.roundToInt

object WeatherUnitsConverter {
    // API always returns metric (Celsius + m/s), convert from Celsius if needed
    fun convertTemp(celsius: Double, unit: String): Double {
        val result = when (unit.uppercase()) {
            "C" -> celsius
            "F" -> celsius * 1.8 + 32
            "K" -> celsius + 273.15
            else -> celsius
        }
        return (result * 10.0).roundToInt() / 10.0
    }

    // API always returns metric (m/s), convert to mph if needed
    fun convertWindSpeed(mps: Double, unit: String): Double {
        val result = when (unit.uppercase()) {
            "MPH" -> mps * 2.237
            else -> mps // "MS" or default → keep m/s
        }
        return (result * 10.0).roundToInt() / 10.0
    }
}