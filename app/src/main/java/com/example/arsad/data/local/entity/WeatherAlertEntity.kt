package com.example.arsad.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "weather_alerts")
data class WeatherAlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lat: Double,
    val lon: Double,
    val locationName: String,
    val startTime: Long,
    val endTime: Long,
    val alertType: String,  // Type (Notification vs Alarm)
    val isEnabled: Boolean = true
)