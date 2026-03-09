package com.example.arsad.presentation.home.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.data.remote.responses.WeatherResponse
import com.example.arsad.util.getWindSymbol
import com.example.arsad.util.localize


@Composable
fun WeatherDetailsGrid(weather: WeatherResponse, windUnit: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val windSymbol = getWindSymbol(windUnit)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.section_weather_details),
            style = typography.headlineSmall,
            color = colors.onBackground,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailCard(
                Modifier.weight(1f),
                Icons.Default.WaterDrop,
                stringResource(R.string.label_humidity),
                "${weather.weatherMain.humidity.localize()}%"
            )
            DetailCard(
                Modifier.weight(1f),
                Icons.Default.Compress,
                stringResource(R.string.label_pressure),
                "${weather.weatherMain.pressure.localize()} ${stringResource(R.string.pressure_unit)}"
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailCard(
                Modifier.weight(1f),
                Icons.Default.Cloud,
                stringResource(R.string.label_clouds),
                "${weather.clouds.all.localize()}%"
            )
            DetailCard(
                Modifier.weight(1f),
                Icons.Default.Air,
                stringResource(R.string.label_wind),
                "${weather.wind.speed.localize()} $windSymbol"
            )
        }
    }
}

@Composable
private fun DetailCard(modifier: Modifier, icon: ImageVector, label: String, value: String) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = modifier.aspectRatio(1.15f),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.08f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = label,
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value, style = typography.headlineSmall, color = colors.onSurface
            )
        }
    }
}