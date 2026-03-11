package com.example.arsad.presentation.home.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.data.models.HourlyWeatherModel
import com.example.arsad.util.formatTo12Hour
import com.example.arsad.util.getTempSymbol
import com.example.arsad.util.getWeatherIcon
import com.example.arsad.util.localize

@Composable
fun HourlyForecastSection(
    hourlyData: List<HourlyWeatherModel>,
    modifier: Modifier = Modifier,
    tempUnit: String
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.section_today_hourly),
            style = typography.headlineSmall,
            color = colors.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(hourlyData) { item ->
                HourlyCard(item = item, tempUnit)
            }
        }
    }
}

@Composable
private fun HourlyCard(
    item: HourlyWeatherModel,
    tempUnit: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val tempSymbol = getTempSymbol(tempUnit)

    Surface(
        modifier = modifier
            .width(80.dp)
            .height(120.dp),
        shape = RoundedCornerShape(20.dp),
        color = colors.surface.copy(alpha = 0.5f),
        tonalElevation = 4.dp,
        border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formatTo12Hour(item.dtTxt),
                style = typography.labelSmall,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = getWeatherIcon(item.iconCode)),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${item.temp.toInt().localize()} $tempSymbol",
                style = typography.labelLarge,
                color = colors.onSurface
            )
        }
    }
}