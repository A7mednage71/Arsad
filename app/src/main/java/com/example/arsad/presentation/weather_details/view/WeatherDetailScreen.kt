package com.example.arsad.presentation.weather_details.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arsad.presentation.home.view.WeatherDetailsContent
import com.example.arsad.presentation.home.view.components.FailureDisplay
import com.example.arsad.presentation.home.view.components.WeatherDetailsShimmerLoading
import com.example.arsad.presentation.weather_details.viewModel.WeatherDetailState
import com.example.arsad.presentation.weather_details.viewModel.WeatherDetailViewModel

@Composable
fun WeatherDetailScreen(
    lat: Double,
    lon: Double,
    id: Int,
    viewModel: WeatherDetailViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.weatherDetailState.collectAsStateWithLifecycle()

    LaunchedEffect(lat, lon) {
        viewModel.fetchWeatherDetails(lat, lon, id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        when (val result = state) {
            is WeatherDetailState.Loading -> WeatherDetailsShimmerLoading()
            is WeatherDetailState.Success -> WeatherDetailsContent(data = result.data)
            is WeatherDetailState.Error -> FailureDisplay(
                message = result.message,
                onRetry = { viewModel.fetchWeatherDetails(lat, lon, id) }
            )
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}