package com.example.arsad.data.mapper

import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.models.AlertType
import com.example.arsad.data.models.WeatherAlertModel
import com.example.arsad.util.formatToWeatherAlertTime


fun WeatherAlertEntity.toUIModel(lang: String): WeatherAlertModel {
    return WeatherAlertModel(
        id = this.id,
        from = this.startTime.formatToWeatherAlertTime(lang),
        to = this.endTime.formatToWeatherAlertTime(lang),
        alertType = if (this.alertType == "ALARM") AlertType.ALARM else AlertType.NOTIFICATION,
        isActive = this.isEnabled,
        startTimeMillis = this.startTime,
        endTimeMillis = this.endTime,
        locationName = this.locationName
    )
}
