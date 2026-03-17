package com.example.arsad.presentation.glance_app_widget

import androidx.datastore.preferences.core.stringPreferencesKey

object WeatherGlanceWidgetKeys {
    val CITY_NAME = stringPreferencesKey("city_name")
    val TEMP = stringPreferencesKey("temp")
    val DESCRIPTION = stringPreferencesKey("description")
    val HUMIDITY = stringPreferencesKey("humidity")
    val WIND_SPEED = stringPreferencesKey("wind_speed")
    val PRESSURE = stringPreferencesKey("pressure")
    val ICON_CODE = stringPreferencesKey("icon_code")
    val SELECTED_LANGUAGE =
        stringPreferencesKey("selected_language")
    val ERROR_MSG = stringPreferencesKey("error_msg")

}