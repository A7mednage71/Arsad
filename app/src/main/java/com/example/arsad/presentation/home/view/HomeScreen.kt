package com.example.arsad.presentation.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arsad.presentation.home.view.components.DailyForecast
import com.example.arsad.presentation.home.view.components.FiveDayForecastSection
import com.example.arsad.presentation.home.view.components.HourlyForecast
import com.example.arsad.presentation.home.view.components.HourlyForecastSection
import com.example.arsad.presentation.home.view.components.MainWeatherCard
import com.example.arsad.presentation.home.view.components.WeatherDetail
import com.example.arsad.presentation.home.view.components.WeatherDetailsGrid
import com.example.arsad.ui.theme.ArsadGradient

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme

    // Dummy weather details
    val weatherDetails = listOf(
        WeatherDetail("Humidity", "65%", Icons.Default.WaterDrop),
        WeatherDetail("Pressure", "1013 hPa", Icons.Default.Compress),
        WeatherDetail("Clouds", "40%", Icons.Default.Cloud),
        WeatherDetail("Wind", "12 km/h", Icons.Default.Air)
    )

    // Dummy hourly forecast
    val hourlyData = listOf(
        HourlyForecast("Now", "23°", Icons.Default.WbSunny),
        HourlyForecast("1 PM", "24°", Icons.Default.WbSunny),
        HourlyForecast("2 PM", "25°", Icons.Default.WbCloudy),
        HourlyForecast("3 PM", "24°", Icons.Default.WbCloudy),
        HourlyForecast("4 PM", "23°", Icons.Default.Cloud),
        HourlyForecast("5 PM", "22°", Icons.Default.Cloud),
        HourlyForecast("6 PM", "21°", Icons.Default.NightsStay),
        HourlyForecast("7 PM", "20°", Icons.Default.NightsStay)
    )

    // Dummy 5-day forecast
    val dailyData = listOf(
        DailyForecast("Saturday", "25°", "18°", Icons.Default.WbSunny, "Sunny"),
        DailyForecast("Sunday", "23°", "17°", Icons.Default.WbCloudy, "Cloudy"),
        DailyForecast("Monday", "20°", "15°", Icons.Default.Cloud, "Overcast"),
        DailyForecast("Tuesday", "22°", "16°", Icons.Default.WbSunny, "Clear"),
        DailyForecast("Wednesday", "24°", "17°", Icons.Default.WbCloudy, "Partly Cloudy")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ArsadGradient.screenBackground)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Cairo, Egypt",
                style = typography.headlineMedium,
                color = colors.onBackground
            )
            Text(
                text = "Thursday, March 6",
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            MainWeatherCard(
                temp = "23°",
                status = "Partly Cloudy",
                feelsLike = "Feels like clear sky"
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            WeatherDetailsGrid(details = weatherDetails)
            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            HourlyForecastSection(hourlyData = hourlyData)
            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            FiveDayForecastSection(dailyData = dailyData)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
