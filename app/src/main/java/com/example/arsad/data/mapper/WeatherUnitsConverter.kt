package com.example.arsad.data.mapper

object WeatherUnitsConverter {
    fun convertTemp(kelvin: Double, unit: String): Double {
        return when (unit) {
            "C" -> kelvin - 273.15
            "F" -> (kelvin - 273.15) * 1.8 + 32
            else -> kelvin
        }
    }

    fun convertWindSpeed(mps: Double, unit: String): Double {
        return when (unit) {
            "mph" -> mps * 2.237
            else -> mps
        }
    }
}