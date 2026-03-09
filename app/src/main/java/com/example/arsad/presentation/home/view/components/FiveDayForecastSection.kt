package com.example.arsad.presentation.home.view.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.data.models.ForecastItem
import com.example.arsad.util.formatToOnlyDayName
import com.example.arsad.util.getTempSymbol
import com.example.arsad.util.getWeatherIcon
import com.example.arsad.util.getWindSymbol
import com.example.arsad.util.localize

@Composable
fun FiveDayForecastSection(
    dailyData: List<ForecastItem>,
    tempUnit: String,
    windUnit: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.section_five_day),
            style = typography.headlineSmall,
            color = colors.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.surface.copy(alpha = 0.5f),
            tonalElevation = 4.dp,
            border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.08f))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .animateContentSize(animationSpec = tween(300))
            ) {
                dailyData.forEachIndexed { index, item ->
                    DailyRow(forecastItem = item, tempUnit = tempUnit, windUnit = windUnit)
                    if (index < dailyData.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            thickness = 0.6.dp,
                            color = colors.onSurface.copy(alpha = 0.08f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyRow(
    forecastItem: ForecastItem,
    tempUnit: String,
    windUnit: String,
    modifier: Modifier = Modifier
) {
    val windSymbol = getWindSymbol(windUnit)
    val tempSymbol = getTempSymbol(tempUnit)

    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = tween(300))) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = forecastItem.dt.formatToOnlyDayName(),
                style = typography.bodyLarge,
                color = colors.onSurface,
                modifier = Modifier.width(100.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = getWeatherIcon(forecastItem.weather.firstOrNull()?.icon)),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = forecastItem.weather.firstOrNull()?.description ?: "",
                    style = typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }

            Text(
                text = "${
                    forecastItem.main.tempMax.toInt().localize()
                }/ ${forecastItem.main.tempMin.toInt().localize()} $tempSymbol",
                style = typography.labelLarge,
                color = colors.onSurface
            )

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = colors.onSurfaceVariant,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(20.dp)
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colors.surfaceVariant.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DetailChip(
                        icon = Icons.Default.WaterDrop,
                        label = stringResource(R.string.label_humidity),
                        value = "${forecastItem.main.humidity.localize()}%"
                    )
                    DetailChip(
                        icon = Icons.Default.Air,
                        label = stringResource(R.string.label_wind),
                        value = "${forecastItem.wind.speed.localize()} $windSymbol"
                    )
                    DetailChip(
                        icon = Icons.Default.Compress,
                        label = stringResource(R.string.label_pressure),
                        value = forecastItem.main.pressure.localize() + " " + stringResource(R.string.pressure_unit)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailChip(
    icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = colors.secondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value, style = typography.labelLarge, color = colors.onSurface
        )
        Text(
            text = label, style = typography.labelSmall, color = colors.onSurfaceVariant
        )
    }
}