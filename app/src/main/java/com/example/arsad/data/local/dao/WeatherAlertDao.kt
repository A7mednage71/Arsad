package com.example.arsad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.arsad.data.local.entity.WeatherAlertEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherAlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: WeatherAlertEntity)

    @Query("DELETE FROM weather_alerts WHERE id = :alertId")
    suspend fun deleteAlertById(alertId: Int)

    @Query("SELECT * FROM weather_alerts")
    fun getAllAlerts(): Flow<List<WeatherAlertEntity>>

    @Query("UPDATE weather_alerts SET isEnabled = :isEnabled WHERE id = :alertId")
    suspend fun updateAlertStatus(alertId: Int, isEnabled: Boolean)

    @Query("SELECT * FROM weather_alerts WHERE id = :alertId")
    suspend fun getAlertById(alertId: Int): WeatherAlertEntity?
}