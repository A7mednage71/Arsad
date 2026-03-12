package com.example.arsad.data.mapper

import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.models.AlertType
import com.example.arsad.data.models.WeatherAlertModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun WeatherAlertEntity.toUIModel(): WeatherAlertModel {
    val dateFormatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    return WeatherAlertModel(
        id = this.id,
        fromDate = dateFormatter.format(Date(this.startTime)),
        fromTime = timeFormatter.format(Date(this.startTime)),
        toDate = dateFormatter.format(Date(this.endTime)),
        toTime = timeFormatter.format(Date(this.endTime)),
        alertType = if (this.alertType == "ALARM") AlertType.ALARM else AlertType.NOTIFICATION,
        isActive = this.isEnabled,
        endTimeMillis = this.endTime,
        locationName = this.locationName
    )
}
