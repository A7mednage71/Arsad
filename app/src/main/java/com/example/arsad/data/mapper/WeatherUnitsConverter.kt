package com.example.arsad.data.mapper

import kotlin.math.roundToInt

object WeatherUnitsConverter {
    fun convertTemp(kelvin: Double, unit: String): Double {
        val result = when (unit.uppercase()) {
            "C" -> kelvin - 273.15
            "F" -> (kelvin - 273.15) * 1.8 + 32
            else -> kelvin
        }
        return (result * 10.0).roundToInt() / 10.0
    }

    fun convertWindSpeed(mps: Double, unit: String): Double {
        val result = when (unit.uppercase()) {
            "MPH" -> mps * 2.237
            else -> mps
        }
        return (result * 10.0).roundToInt() / 10.0
    }
}