package com.example.arsad.data.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.arsad.MainActivity
import com.example.arsad.R
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.models.AlertType
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.repository.IWeatherRepository
import com.example.arsad.presentation.alerts.view.AlarmActivity
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class WeatherWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: IWeatherRepository,
    private val settingsManager: SettingsManager
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val CHANNEL_ID_NOTIFICATION = "weather_notifications_v2"
        private const val CHANNEL_ID_ALARM = "weather_alarms_v2"

        private const val KEY_ALERT_ID = "ALERT_ID"
        private const val KEY_ALERT_TYPE = "ALERT_TYPE"
    }

    override suspend fun doWork(): Result {
        val alertId = inputData.getInt(KEY_ALERT_ID, -1)
        val alertTypeStr = inputData.getString(KEY_ALERT_TYPE) ?: AlertType.NOTIFICATION.name

        if (alertId == -1) return Result.failure()

        return try {
            val alert = repository.getAlertById(alertId) ?: return Result.success()
            val isAlarm = alertTypeStr == AlertType.ALARM.name
            val currentTime = System.currentTimeMillis()

            if (currentTime > alert.endTime) {
                repository.updateAlertStatus(alertId, false)
                return Result.success()
            }

            // getting the Localized Context so the Title
            // can be translated into the language we extracted.

            val selectedLang = settingsManager.languageFlow.first()
            val localizedContext = getLocalizedContext(applicationContext, selectedLang)
            val titleRes =
                if (isAlarm) R.string.weather_alert_title else R.string.weather_notification_title
            val notificationTitle = localizedContext.getString(titleRes)

            val weatherResponse = repository.getWeather(alert.lat, alert.lon, selectedLang)

            if (weatherResponse is ApiResult.Success) {
                val description = weatherResponse.data.weather.firstOrNull()?.description ?: ""
                val location = weatherResponse.data.name

                if (isWeatherDangerous(description)) {
                    val formattedDescription = description.split(" ").joinToString(" ") {
                        it.replaceFirstChar { char -> char.uppercase() }
                    }
                    val messageText = localizedContext.getString(
                        R.string.weather_message_format,
                        location,
                        formattedDescription
                    )
                    showNotification(
                        id = alertId,
                        title = notificationTitle,
                        message = messageText,
                        isAlarm = isAlarm
                    )
                }
                reScheduleNextCheck(alertId, alertTypeStr)
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun reScheduleNextCheck(id: Int, type: String) {
        val data = workDataOf(KEY_ALERT_ID to id, KEY_ALERT_TYPE to type)
        val nextWork = OneTimeWorkRequestBuilder<WeatherWorker>()
            .setInitialDelay(5, TimeUnit.MINUTES)
            .setInputData(data)
            .addTag("weather_alert_$id")
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "work_$id",
            ExistingWorkPolicy.REPLACE,
            nextWork
        )
    }

    private fun isWeatherDangerous(description: String): Boolean {
        val dangerousKeywords =
            listOf("storm", "rain", "snow", "thunderstorm", "dust", "sand", "clouds")
        return dangerousKeywords.any { description.contains(it, ignoreCase = true) }
    }

    @SuppressLint("FullScreenIntentPolicy")
    private fun showNotification(id: Int, title: String, message: String, isAlarm: Boolean) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = if (isAlarm) CHANNEL_ID_ALARM else CHANNEL_ID_NOTIFICATION
        createNotificationChannel(notificationManager, isAlarm)

        val targetActivity = if (isAlarm) AlarmActivity::class.java else MainActivity::class.java
        val smallIconRes = if (isAlarm) {
            R.drawable.ic_alarm_clock
        } else {
            R.drawable.ic_notification_bold
        }

        val intent = Intent(applicationContext, targetActivity).apply {
            putExtra("LOCATION", title)
            putExtra("DESC", message)
            putExtra("ID", id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (isAlarm) {
            try {
                applicationContext.startActivity(intent)
            } catch (e: Exception) {
                android.util.Log.e("WeatherWorker", "Direct launch failed: ${e.message}")
            }
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(smallIconRes)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.drawable.app_icon
                )
            )
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(if (isAlarm) NotificationCompat.PRIORITY_MAX else NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(if (isAlarm) NotificationCompat.CATEGORY_ALARM else NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (isAlarm) {
            builder.setOngoing(true)
                .setFullScreenIntent(pendingIntent, true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }

        val notification = builder.build()
        if (isAlarm) {
            notification.flags = notification.flags or NotificationCompat.FLAG_INSISTENT
        }

        notificationManager.notify(id, notification)
    }

    private fun createNotificationChannel(manager: NotificationManager, isAlarm: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = if (isAlarm) CHANNEL_ID_ALARM else CHANNEL_ID_NOTIFICATION
            val channelName = if (isAlarm) "Weather Alarms Emergency" else "Weather Notifications"
            val importance =
                if (isAlarm) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel for weather emergency alerts"
                enableVibration(true)
                enableLights(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                if (isAlarm) {
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                    setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                        audioAttributes
                    )
                }
            }
            manager.createNotificationChannel(channel)
        }
    }

    private fun getLocalizedContext(context: Context, lang: String): Context {
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        val config = android.content.res.Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}