package com.example.arsad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.arsad.data.local.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_cache WHERE id = 0")
    suspend fun getCachedWeather(): WeatherEntity?

    @Query("DELETE FROM weather_cache")
    suspend fun clearCache()
}