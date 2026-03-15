package com.example.arsad.repos

import com.example.arsad.data.local.datasource.IWeatherLocalDataSource
import com.example.arsad.data.models.GetWeatherParams
import com.example.arsad.data.remote.datasource.ApiResult
import com.example.arsad.data.remote.datasource.IWeatherRemoteDataSource
import com.example.arsad.data.remote.responses.WeatherResponse
import com.example.arsad.data.repository.WeatherRepositoryImpl
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private val remoteDataSource: IWeatherRemoteDataSource = mockk()
    private val localDataSource: IWeatherLocalDataSource = mockk()
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun getFullWeatherData_remoteSuccess_returnsSuccessAndCachesData() = runTest {
        // Arrange
        val params = GetWeatherParams(30.0, 31.0, "metric", "en", "C", "MS")
        coEvery { remoteDataSource.getCurrentWeather(params) } returns ApiResult.Success(
            mockk(
                relaxed = true
            )
        )

        coEvery { remoteDataSource.getForecast(params) } returns ApiResult.Success(mockk(relaxed = true))
        coEvery { localDataSource.saveWeather(any()) } returns Unit

        // Act
        val result = repository.getFullWeatherData(params, caching = true)

        // Assert
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        coVerify(exactly = 1) { localDataSource.saveWeather(any()) }
    }

    @Test
    fun getFullWeatherData_remoteFailure_returnsLocalCache() = runTest {
        // Arrange
        val params = GetWeatherParams(0.0, 0.0, "metric", "en", "C", "MS")
        coEvery { remoteDataSource.getCurrentWeather(params) } returns ApiResult.Failure("Network Error")
        coEvery { remoteDataSource.getForecast(params) } returns ApiResult.Failure("Network Error")
        coEvery { localDataSource.getCachedWeather() } returns mockk(relaxed = true)

        // Act
        val result = repository.getFullWeatherData(params, caching = true)

        // Assert
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        coVerify { localDataSource.getCachedWeather() }
    }

    @Test
    fun fetchAndSaveLocation_remoteSuccess_insertsLocationToLocal() = runTest {
        // Arrange
        val lat = 30.0
        val lon = 31.0
        val lang = "en"
        val mockResponse = mockk<WeatherResponse>(relaxed = true)

        coEvery { remoteDataSource.getCurrentWeather(any()) } returns ApiResult.Success(mockResponse)
        coEvery { localDataSource.insertSavedLocation(any()) } returns Unit

        // Act
        val result = repository.fetchAndSaveLocation(lat, lon, lang)

        // Assert
        assertThat(result.isSuccess).isTrue()
        coVerify { localDataSource.insertSavedLocation(any()) }
    }
}