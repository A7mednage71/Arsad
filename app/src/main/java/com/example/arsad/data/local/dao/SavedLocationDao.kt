package com.example.arsad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.arsad.data.local.entity.SavedLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedLocationDao {
    @Query("SELECT * FROM saved_locations")
    fun getAllSavedLocations(): Flow<List<SavedLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedLocation(location: SavedLocationEntity)

    @Query("DELETE FROM saved_locations WHERE id = :locationId")
    suspend fun deleteSavedLocation(locationId: Int)

    @Query("UPDATE saved_locations SET lastTemp = :temp, iconCode = :icon, timestamp = :time WHERE id = :id")
    suspend fun updateLocationWeather(id: Int, temp: Double, icon: String, time: Long)
}