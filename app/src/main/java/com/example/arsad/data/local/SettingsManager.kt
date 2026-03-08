package com.example.arsad.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings_pref")

class SettingsManager(private val context: Context) {

    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("app_lang")
        val TEMP_UNIT_KEY = stringPreferencesKey("temp_unit")
        val WIND_UNIT_KEY = stringPreferencesKey("wind_unit")
        val LOCATION_METHOD_KEY = stringPreferencesKey("location_method")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val LATITUDE_KEY = doublePreferencesKey("latitude")
        val LONGITUDE_KEY = doublePreferencesKey("longitude")
        val LOCATION_NAME_KEY = stringPreferencesKey("location_name")
    }


    suspend fun saveLanguage(lang: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = lang }
    }

    suspend fun saveTempUnit(unit: String) {
        context.dataStore.edit { it[TEMP_UNIT_KEY] = unit }
    }

    suspend fun saveWindUnit(unit: String) {
        context.dataStore.edit { it[WIND_UNIT_KEY] = unit }
    }

    suspend fun saveLocationMethod(method: String) {
        context.dataStore.edit { it[LOCATION_METHOD_KEY] = method }
    }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { it[IS_DARK_MODE] = isDark }
    }

    suspend fun saveLocation(lat: Double, lon: Double, name: String) {
        context.dataStore.edit {
            it[LATITUDE_KEY] = lat
            it[LONGITUDE_KEY] = lon
            it[LOCATION_NAME_KEY] = name
        }
    }


    val languageFlow: Flow<String> = context.dataStore.data.map {
        it[LANGUAGE_KEY] ?: "en"
    }

    val tempUnitFlow: Flow<String> = context.dataStore.data.map {
        it[TEMP_UNIT_KEY] ?: "C"
    }

    val windUnitFlow: Flow<String> = context.dataStore.data.map {
        it[WIND_UNIT_KEY] ?: "MS"
    }

    val locationMethodFlow: Flow<String> = context.dataStore.data.map {
        it[LOCATION_METHOD_KEY] ?: "GPS"
    }

    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data.map {
        it[IS_DARK_MODE] ?: false
    }

    val latitudeFlow: Flow<Double?> = context.dataStore.data.map {
        it[LATITUDE_KEY]
    }

    val longitudeFlow: Flow<Double?> = context.dataStore.data.map {
        it[LONGITUDE_KEY]
    }

    val locationNameFlow: Flow<String?> = context.dataStore.data.map {
        it[LOCATION_NAME_KEY]
    }
}