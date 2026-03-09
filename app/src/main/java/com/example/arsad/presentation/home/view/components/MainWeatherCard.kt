package com.example.arsad.presentation.home.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arsad.R
import com.example.arsad.data.remote.responses.WeatherResponse
import com.example.arsad.util.getTempSymbol
import com.example.arsad.util.getWeatherIcon
import com.example.arsad.util.localize

@Composable
fun MainWeatherCard(
    weather: WeatherResponse,
    tempUnit: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val iconResId = getWeatherIcon(weather.weather.firstOrNull()?.icon)
    val tempSymbol = getTempSymbol(tempUnit)
    val description = weather.weather.firstOrNull()?.description
        ?.replaceFirstChar { it.uppercase() } ?: ""

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = description,
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(4.dp))

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Text(
                text = buildAnnotatedString {
                    append(weather.weatherMain.temp.toInt().localize())
                    withStyle(
                        SpanStyle(
                            fontSize = 36.sp,
                            baselineShift = BaselineShift.Superscript,
                            fontWeight = FontWeight.Light
                        )
                    ) { append(tempSymbol) }
                },
                style = typography.displayLarge.copy(
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-2).sp,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                color = colors.onBackground
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = description,
            style = typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            color = colors.onBackground.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            color = colors.secondaryContainer.copy(alpha = 0.45f),
            shape = RoundedCornerShape(20.dp)
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Text(
                    text = "${stringResource(R.string.label_feels_like)}  ${
                        weather.weatherMain.feelsLike.toInt().localize()
                    }$tempSymbol",
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 7.dp),
                    style = typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = colors.onSecondaryContainer
                )
            }
        }
    }
}