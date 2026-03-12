package com.example.arsad.data.mapper

import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.models.SavedLocationModel

fun SavedLocationEntity.toUIModel(): SavedLocationModel {
    return SavedLocationModel(
        id = this.id,
        cityName = this.cityName,
        lat = this.lat,
        lon = this.lon,
        lastTemp = this.lastTemp,
        iconCode = this.iconCode,
        country = this.country,
        timestamp = this.timestamp
    )
}

fun SavedLocationModel.toEntity(): SavedLocationEntity {
    return SavedLocationEntity(
        id = this.id,
        cityName = this.cityName,
        lat = this.lat,
        lon = this.lon,
        lastTemp = this.lastTemp,
        iconCode = this.iconCode,
        country = this.country,
        timestamp = this.timestamp
    )
}