package com.example.arsad.presentation.alerts.model

enum class AlertType { NOTIFICATION, ALARM }

data class WeatherAlert(
    val id: Int,
    val fromTime: String,
    val toTime: String,
    val fromDate: String,
    val toDate: String,
    val alertType: AlertType,
    val isActive: Boolean
)

