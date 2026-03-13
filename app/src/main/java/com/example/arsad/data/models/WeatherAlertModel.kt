package com.example.arsad.data.models

enum class AlertType { NOTIFICATION, ALARM }

data class WeatherAlertModel(
    val id: Int,
    val locationName: String,
    val fromTime: String,
    val toTime: String,
    val fromDate: String,
    val toDate: String,
    val alertType: AlertType,
    val isActive: Boolean,
    val startTimeMillis: Long,
    val endTimeMillis: Long
)

