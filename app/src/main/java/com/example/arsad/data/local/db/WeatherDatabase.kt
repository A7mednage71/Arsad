package com.example.arsad.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.arsad.data.local.converters.WeatherTypeConverters
import com.example.arsad.data.local.dao.SavedLocationDao
import com.example.arsad.data.local.dao.WeatherAlertDao
import com.example.arsad.data.local.dao.WeatherDao
import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.local.entity.WeatherEntity


@Database(
    entities = [WeatherEntity::class, SavedLocationEntity::class, WeatherAlertEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(WeatherTypeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun savedLocationDao(): SavedLocationDao

    abstract fun weatherAlertDao(): WeatherAlertDao


    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, WeatherDatabase::class.java, "weather_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}