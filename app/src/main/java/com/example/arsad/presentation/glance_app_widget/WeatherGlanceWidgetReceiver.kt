package com.example.arsad.presentation.glance_app_widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.arsad.data.worker.WeatherGlanceSyncWorker

class WeatherGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ArsadGlanceWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        triggerWeatherSync(context)
    }

    private fun triggerWeatherSync(context: Context) {
        try {
            val syncRequest = OneTimeWorkRequestBuilder<WeatherGlanceSyncWorker>()
                .addTag("widget_initial_sync")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "widget_sync_initial",
                androidx.work.ExistingWorkPolicy.REPLACE,
                syncRequest
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}