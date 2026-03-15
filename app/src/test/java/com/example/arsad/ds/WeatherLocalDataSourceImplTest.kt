package com.example.arsad.ds

import com.example.arsad.data.local.dao.SavedLocationDao
import com.example.arsad.data.local.dao.WeatherAlertDao
import com.example.arsad.data.local.dao.WeatherDao
import com.example.arsad.data.local.datasource.WeatherLocalDataSourceImpl
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.local.entity.WeatherEntity
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherLocalDataSourceImplTest {

    private val weatherDao: WeatherDao = mockk()
    private val savedLocationDao: SavedLocationDao = mockk()
    private val weatherAlertDao: WeatherAlertDao = mockk()

    private lateinit var dataSource: WeatherLocalDataSourceImpl

    @Before
    fun setUp() {
        dataSource = WeatherLocalDataSourceImpl(weatherDao, savedLocationDao, weatherAlertDao)
    }

    // Testing WeatherDao Interaction
    @Test
    fun saveWeather_validEntity_callsDaoInsert() = runTest {
        // Arrange
        val weatherEntity = mockk<WeatherEntity>()
        coEvery { weatherDao.insertWeather(any()) } returns Unit

        // Act
        dataSource.saveWeather(weatherEntity)

        // Assert
        coVerify(exactly = 1) { weatherDao.insertWeather(weatherEntity) }
    }

    // Testing WeatherAlertDao Interaction (Returns a Value)
    @Test
    fun insertAlert_validEntity_returnsRowIdFromDao() = runTest {
        // Arrange
        val alertEntity = mockk<WeatherAlertEntity>()
        val expectedId = 101L
        coEvery { weatherAlertDao.insertAlert(any()) } returns expectedId

        // Act
        val resultId = dataSource.insertAlert(alertEntity)

        // Assert
        assertThat(resultId).isEqualTo(expectedId)
        coVerify(exactly = 1) { weatherAlertDao.insertAlert(alertEntity) }
    }

    //Testing SavedLocationDao Interaction
    @Test
    fun deleteSavedLocation_validId_callsDaoDelete() = runTest {
        // Arrange
        val locationId = 5
        coEvery { savedLocationDao.deleteSavedLocation(any()) } returns Unit

        // Act
        dataSource.deleteSavedLocation(locationId)

        // Assert
        coVerify(exactly = 1) { savedLocationDao.deleteSavedLocation(locationId) }
    }
}