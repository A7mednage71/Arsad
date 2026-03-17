package com.example.arsad.data.worker

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.arsad.R
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.repository.IWeatherRepository
import com.example.arsad.presentation.glance_app_widget.ArsadGlanceWidget
import com.example.arsad.presentation.glance_app_widget.WeatherGlanceWidgetKeys
import com.example.arsad.util.getLocalizedContext
import kotlinx.coroutines.flow.first
import java.io.IOException

class WeatherGlanceSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val repository: IWeatherRepository,
    private val settingsManager: SettingsManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(applicationContext)
        val glanceIds = manager.getGlanceIds(ArsadGlanceWidget::class.java)

        if (glanceIds.isEmpty()) return Result.success()

        val lang = settingsManager.languageFlow.first()
        val localizedContext = applicationContext.getLocalizedContext(lang)

        return try {
            val lat = settingsManager.latitudeFlow.first()
            val lon = settingsManager.longitudeFlow.first()

            if (lat == null || lon == null) {
                updateErrorState(
                    glanceIds,
                    localizedContext.getString(R.string.no_location_title)
                )
                return Result.failure()
            }

            when (val result = repository.getWeather(lat, lon, lang)) {
                is ApiResult.Success -> {
                    val data = result.data
                    glanceIds.forEach { id ->
                        updateAppWidgetState(applicationContext, id) { prefs ->
                            prefs[WeatherGlanceWidgetKeys.TEMP] =
                                data.weatherMain.temp.toInt().toString()
                            prefs[WeatherGlanceWidgetKeys.CITY_NAME] = data.name
                            prefs[WeatherGlanceWidgetKeys.DESCRIPTION] =
                                data.weather.firstOrNull()?.description ?: ""
                            prefs[WeatherGlanceWidgetKeys.HUMIDITY] = "${data.weatherMain.humidity}"
                            prefs[WeatherGlanceWidgetKeys.WIND_SPEED] = "${data.wind.speed}"
                            prefs[WeatherGlanceWidgetKeys.PRESSURE] =
                                data.weatherMain.pressure.toString()
                            prefs[WeatherGlanceWidgetKeys.ICON_CODE] =
                                data.weather.firstOrNull()?.icon ?: ""
                            prefs[WeatherGlanceWidgetKeys.SELECTED_LANGUAGE] = lang

                            prefs.remove(WeatherGlanceWidgetKeys.ERROR_MSG)
                        }
                        ArsadGlanceWidget().update(applicationContext, id)
                    }
                    Result.success()
                }

                is ApiResult.Failure -> {
                    updateErrorState(glanceIds, localizedContext.getString(R.string.error_unknown))
                    Result.retry()
                }

                else -> Result.retry()
            }
        } catch (e: Exception) {
            val errorMsg = when (e) {
                is IOException -> localizedContext.getString(R.string.error_no_internet)
                is SecurityException -> localizedContext.getString(R.string.error_no_location)
                else -> localizedContext.getString(R.string.error_unknown)
            }

            updateErrorState(glanceIds, errorMsg)
            if (e is SecurityException) Result.failure() else Result.retry()
        }
    }

    private suspend fun updateErrorState(
        glanceIds: List<androidx.glance.GlanceId>,
        message: String
    ) {
        glanceIds.forEach { id ->
            updateAppWidgetState(applicationContext, id) { prefs ->
                prefs[WeatherGlanceWidgetKeys.ERROR_MSG] = message
            }
            ArsadGlanceWidget().update(applicationContext, id)
        }
    }
}