package com.example.arsad.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.arsad.R

fun getWeatherIcon(iconCode: String?): Int {
    return when (iconCode) {
        "01d" -> R.drawable.sunny
        "01n" -> R.drawable.clear_night
        "02d" -> R.drawable.partly_cloudy
        "02n" -> R.drawable.partly_cloudy_night
        "03d", "03n" -> R.drawable.cloudy
        "04d", "04n" -> R.drawable.overcast
        "09d", "09n" -> R.drawable.drizzle
        "10d" -> R.drawable.rainy_day
        "10n" -> R.drawable.rainy_night
        "11d", "11n" -> R.drawable.thunderstorm
        "13d", "13n" -> R.drawable.snowy
        "50d", "50n" -> R.drawable.foggy
        else -> R.drawable.sunny
    }
}


@Composable
fun getTempSymbol(unit: String): String {
    return when (unit) {
        "C" -> stringResource(R.string.temp_celsius)
        "F" -> stringResource(R.string.temp_fahrenheit)
        else -> stringResource(R.string.temp_kelvin)
    }
}

@Composable
fun getWindSymbol(unit: String): String {
    return when (unit) {
        "mph" -> stringResource(R.string.wind_mph)
        else -> stringResource(R.string.wind_ms)
    }
}