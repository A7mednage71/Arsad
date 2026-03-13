package com.example.arsad.data.worker

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
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.arsad.MainActivity
import com.example.arsad.R
import com.example.arsad.data.models.AlertType
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.repository.IWeatherRepository
import java.util.concurrent.TimeUnit

class WeatherWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: IWeatherRepository
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val CHANNEL_ID_NOTIFICATION = "weather_notifications_channel"
        private const val CHANNEL_ID_ALARM = "weather_alarms_channel"

        private const val KEY_ALERT_ID = "ALERT_ID"
        private const val KEY_ALERT_TYPE = "ALERT_TYPE"
    }

    override suspend fun doWork(): Result {
        val alertId = inputData.getInt(KEY_ALERT_ID, -1)
        val alertTypeStr = inputData.getString(KEY_ALERT_TYPE) ?: AlertType.NOTIFICATION.name

        if (alertId == -1) return Result.failure()

        return try {
            val alert = repository.getAlertById(alertId) ?: return Result.success()
            val currentTime = System.currentTimeMillis()

            if (currentTime > alert.endTime) {
                repository.updateAlertStatus(alertId, false)
                return Result.success()
            }

            val weatherResponse = repository.getWeather(alert.lat, alert.lon)

            if (weatherResponse is ApiResult.Success) {
                val description = weatherResponse.data.weather.firstOrNull()?.description ?: ""

                if (isWeatherDangerous(description)) {
                    val formattedDescription = description.split(" ").joinToString(" ") {
                        it.replaceFirstChar { char -> char.uppercase() }
                    }
                    showNotification(
                        id = alertId,
                        title = "Weather Alert",
                        message = "In ${alert.locationName}: $formattedDescription",
                        isAlarm = alertTypeStr == AlertType.ALARM.name
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

    private fun showNotification(id: Int, title: String, message: String, isAlarm: Boolean) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = if (isAlarm) CHANNEL_ID_ALARM else CHANNEL_ID_NOTIFICATION
        createNotificationChannel(notificationManager, isAlarm)

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeIcon =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.app_icon)

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification_bold)
            .setLargeIcon(largeIcon)
            .setColor(ContextCompat.getColor(applicationContext, R.color.purple_500))
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(if (isAlarm) NotificationCompat.PRIORITY_MAX else NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(if (isAlarm) NotificationCompat.CATEGORY_ALARM else NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (isAlarm) {
            builder.setOngoing(true) //  (Un-swipeable)
                .setFullScreenIntent(pendingIntent, true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setTimeoutAfter(2 * 60 * 1000)
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
            val channelName =
                if (isAlarm) "Weather Alarms (High Priority)" else "Weather Notifications"
            val importance =
                if (isAlarm) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Weather alerts channel"
                enableVibration(true)
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
}