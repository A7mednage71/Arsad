package com.example.arsad.presentation.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arsad.data.models.WeatherModel
import com.example.arsad.presentation.home.view.components.FiveDayForecastSection
import com.example.arsad.presentation.home.view.components.HomeErrorState
import com.example.arsad.presentation.home.view.components.HomeShimmerLoading
import com.example.arsad.presentation.home.view.components.HourlyForecastSection
import com.example.arsad.presentation.home.view.components.MainWeatherCard
import com.example.arsad.presentation.home.view.components.WeatherDetailsGrid
import com.example.arsad.presentation.home.viewModel.HomeUiState
import com.example.arsad.presentation.home.viewModel.HomeViewModel
import com.example.arsad.util.formatTimestamp

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is HomeUiState.Loading -> HomeShimmerLoading()
            is HomeUiState.Error -> HomeErrorState(
                message = state.message,
                onRetry = { homeViewModel.refresh() }
            )

            is HomeUiState.Success -> HomeSuccessContent(data = state.data)
        }
    }
}

@Composable
private fun HomeSuccessContent(data: WeatherModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            HeaderSection(cityName = data.cityName, timestamp = data.timestamp)
        }
        item {
            MainWeatherCard(data = data, tempUnit = data.tempUnit)
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            WeatherDetailsGrid(data = data, windUnit = data.windUnit)
            Spacer(modifier = Modifier.height(28.dp))
        }
        item {
            HourlyForecastSection(hourlyData = data.hourlyForecast, tempUnit = data.tempUnit)
            Spacer(modifier = Modifier.height(28.dp))
        }
        item {
            FiveDayForecastSection(
                dailyData = data.dailyForecast,
                tempUnit = data.tempUnit,
                windUnit = data.windUnit
            )
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Composable
private fun HeaderSection(cityName: String, timestamp: Long) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = cityName,
            style = typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = timestamp.formatTimestamp(),
            style = typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}