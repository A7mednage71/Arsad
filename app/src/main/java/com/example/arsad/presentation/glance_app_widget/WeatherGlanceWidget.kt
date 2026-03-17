package com.example.arsad.presentation.glance_app_widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.arsad.presentation.glance_app_widget.components.WeatherGlanceWidgetFailure
import com.example.arsad.presentation.glance_app_widget.components.WeatherGlanceWidgetLoading
import com.example.arsad.presentation.glance_app_widget.components.WeatherGlanceWidgetSuccessContent

class ArsadGlanceWidget : GlanceAppWidget() {
    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WeatherWidgetContent()
        }
    }

    @Composable
    fun WeatherWidgetContent() {
        val prefs = currentState<Preferences>()
        val cityName = prefs[WeatherGlanceWidgetKeys.CITY_NAME]
        val errorMsg = prefs[WeatherGlanceWidgetKeys.ERROR_MSG]

        when (cityName) {
            null if errorMsg != null -> {
                WeatherGlanceWidgetFailure(prefs, errorMsg)
            }

            null -> {
                WeatherGlanceWidgetLoading(prefs)
            }

            else -> {
                WeatherGlanceWidgetSuccessContent(prefs)
            }
        }
    }
}