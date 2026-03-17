package com.example.arsad.presentation.glance_app_widget.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.arsad.MainActivity
import com.example.arsad.R
import com.example.arsad.presentation.glance_app_widget.WeatherGlanceWidgetKeys
import com.example.arsad.ui.theme.WidgetAccentColor
import com.example.arsad.ui.theme.WidgetFooterBackground
import com.example.arsad.ui.theme.WidgetPrimaryText
import com.example.arsad.ui.theme.WidgetSecondaryText
import com.example.arsad.util.getLocalizedContext
import com.example.arsad.util.getWeatherIcon
import com.example.arsad.util.localize

@SuppressLint("RestrictedApi")
@Composable
fun WeatherGlanceWidgetSuccessContent(prefs: Preferences) {
    val context = LocalContext.current

    val selectedLang = prefs[WeatherGlanceWidgetKeys.SELECTED_LANGUAGE] ?: "en"
    val localizedContext = context.getLocalizedContext(selectedLang)
    val isArabic = selectedLang == "ar"

    val cityName = prefs[WeatherGlanceWidgetKeys.CITY_NAME]
        ?: localizedContext.getString(R.string.home_location_placeholder)

    val temp = (prefs[WeatherGlanceWidgetKeys.TEMP] ?: "--").localize(selectedLang)
    val desc = prefs[WeatherGlanceWidgetKeys.DESCRIPTION]
        ?: localizedContext.getString(R.string.weather_clear)

    val windValue = (prefs[WeatherGlanceWidgetKeys.WIND_SPEED] ?: "0").localize(selectedLang)
    val wind = "$windValue ${localizedContext.getString(R.string.wind_ms)}"

    val presValue = (prefs[WeatherGlanceWidgetKeys.PRESSURE] ?: "1013").localize(selectedLang)
    val pres = "$presValue ${localizedContext.getString(R.string.pressure_unit)}"

    val humValue = (prefs[WeatherGlanceWidgetKeys.HUMIDITY] ?: "0").localize(selectedLang)
    val hum = "$humValue %"

    val iconCode = prefs[WeatherGlanceWidgetKeys.ICON_CODE] ?: "01d"

    val icon = getWeatherIcon(iconCode)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ImageProvider(R.drawable.widget_bg_gradient))
            .appWidgetBackground()
            .cornerRadius(28.dp)
            .padding(16.dp)
            .clickable(actionStartActivity<MainActivity>()),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = cityName,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WidgetPrimaryText
                    ),
                    maxLines = 1
                )
                Text(
                    text = localizedContext.getString(R.string.current_weather_label),
                    style = TextStyle(fontSize = 11.sp, color = WidgetSecondaryText)
                )
            }
            Image(
                provider = ImageProvider(icon),
                contentDescription = null,
                modifier = GlanceModifier.size(38.dp),
            )
        }

        Spacer(modifier = GlanceModifier.defaultWeight())

        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            val tempUnit =
                if (isArabic) localizedContext.getString(R.string.temp_celsius) else "°"
            Text(
                text = "$temp$tempUnit",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = WidgetPrimaryText
                )
            )
            Spacer(modifier = GlanceModifier.width(12.dp))
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = desc,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = WidgetAccentColor
                    ),
                    maxLines = 2
                )
            }
        }

        Spacer(modifier = GlanceModifier.defaultWeight())

        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(WidgetFooterBackground)
                .cornerRadius(16.dp)
                .padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Box(
                modifier = GlanceModifier.defaultWeight(),
                contentAlignment = Alignment.Center
            ) {
                WidgetDetailItem(
                    R.drawable.ic_humidity, hum,
                    localizedContext.getString(R.string.label_humidity),
                    WidgetAccentColor, WidgetPrimaryText, WidgetSecondaryText
                )
            }
            Box(
                modifier = GlanceModifier.defaultWeight(),
                contentAlignment = Alignment.Center
            ) {
                WidgetDetailItem(
                    R.drawable.ic_wind, wind,
                    localizedContext.getString(R.string.label_wind),
                    WidgetAccentColor, WidgetPrimaryText, WidgetSecondaryText
                )
            }
            Box(
                modifier = GlanceModifier.defaultWeight(),
                contentAlignment = Alignment.Center
            ) {
                WidgetDetailItem(
                    R.drawable.ic_pressure, pres,
                    localizedContext.getString(R.string.label_pressure),
                    WidgetAccentColor, WidgetPrimaryText, WidgetSecondaryText
                )
            }
        }
    }
}
