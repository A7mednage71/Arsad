package com.example.arsad.db


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.arsad.data.local.dao.SavedLocationDao
import com.example.arsad.data.local.db.WeatherDatabase
import com.example.arsad.data.local.entity.SavedLocationEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTest {

    private lateinit var db: WeatherDatabase
    private lateinit var dao: SavedLocationDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Create an In-Memory database (Temporary, wiped after test)
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.savedLocationDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    //  Inserting and Reading a Location
    @Test
    fun insertAndReadSavedLocation() = runTest {
        // Arrange
        val location = SavedLocationEntity(
            cityName = "Cairo", lat = 30.0, lon = 31.0,
            lastTemp = 25.0, iconCode = "01d", country = "Egypt",
            timestamp = System.currentTimeMillis()
        )

        // Act
        dao.insertSavedLocation(location)
        val allLocations = dao.getAllSavedLocations().first()

        // Assert using truth
        assertThat(allLocations).isNotEmpty()
        assertThat(allLocations[0].cityName).isEqualTo("Cairo")
        assertThat(allLocations[0].lat).isEqualTo(30.0)
    }

    //  Updating a Location Weather
    @Test
    fun updateLocationWeather_updatesDataCorrectly() = runTest {
        // Arrange
        val location = SavedLocationEntity(
            id = 1,
            cityName = "Alex",
            lat = 31.0,
            lon = 29.0,
            country = "Egypt",
            iconCode = "01d",
            lastTemp = 30.0,
            timestamp = System.currentTimeMillis()
        )
        dao.insertSavedLocation(location)

        // Act
        val newTemp = 18.0
        val newIcon = "02n"
        val newTime = System.currentTimeMillis()
        dao.updateLocationWeather(1, newTemp, newIcon, newTime)

        val updatedLocation = dao.getAllSavedLocations().first()[0]

        // Assert
        assertThat(updatedLocation.lastTemp).isEqualTo(newTemp)
        assertThat(updatedLocation.iconCode).isEqualTo(newIcon)
    }

    //  Deleting a Location
    @Test
    fun deleteLocation_removesFromDatabase() = runTest {
        // Arrange
        val location = SavedLocationEntity(
            id = 1,
            cityName = "Alex",
            lat = 31.0,
            lon = 29.0,
            country = "Egypt",
            iconCode = "01d",
            lastTemp = 30.0,
            timestamp = System.currentTimeMillis()
        )

        dao.insertSavedLocation(location)

        // Act
        dao.deleteSavedLocation(1)
        val allLocations = dao.getAllSavedLocations().first()

        // Assert
        assertThat(allLocations).isEmpty()
    }
}