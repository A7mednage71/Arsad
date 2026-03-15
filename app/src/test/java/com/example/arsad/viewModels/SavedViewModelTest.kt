package com.example.arsad.viewModels

import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.local.entity.SavedLocationEntity
import com.example.arsad.data.models.Coordinates
import com.example.arsad.data.repository.IWeatherRepository
import com.example.arsad.presentation.saved.viewModel.SavedViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) : TestRule {
    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(dispatcher)
                try {
                    base.evaluate()
                } finally {
                    Dispatchers.resetMain()
                }
            }
        }
}

class SavedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks
    private val repository: IWeatherRepository = mockk(relaxed = true)
    private val settingsManager: SettingsManager = mockk(relaxed = true)
    private lateinit var viewModel: SavedViewModel

    @Before
    fun setUp() {
        val entity = SavedLocationEntity(
            id = 1, cityName = "Alex", lat = 31.0, lon = 29.0,
            lastTemp = 20.0, iconCode = "01d", country = "EG",
            timestamp = System.currentTimeMillis()
        )

        every { repository.getAllSavedLocations() } returns flowOf(listOf(entity))
        every { settingsManager.tempUnitFlow } returns flowOf("C")
        every { settingsManager.languageFlow } returns flowOf("en")

        viewModel = SavedViewModel(repository, settingsManager)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun init_loadsLocationsAndConvertsTemperature() = runTest {
        // Act
        advanceUntilIdle()

        // Assert
        val result = viewModel.savedLocations.value

        assertThat(result).isNotEmpty()
        assertThat(result[0].cityName).isEqualTo("Alex")

        assertThat(viewModel.isLoading.value).isFalse()
    }

    @Test
    fun addSavedLocation_callsRepositoryWithCorrectData() = runTest {
        // Arrange
        val cords = Coordinates(30.0, 31.0)
        every { settingsManager.languageFlow } returns flowOf("ar")

        // Act
        viewModel.addSavedLocation(cords)

        // Assert
        coVerify(exactly = 1) {
            repository.fetchAndSaveLocation(30.0, 31.0, "ar")
        }
    }

    @Test
    fun deleteSavedLocation_callsRepositoryToDelete() = runTest {
        // Arrange
        val locationId = 99

        // Act
        viewModel.deleteSavedLocation(locationId)

        // Assert
        coVerify(exactly = 1) {
            repository.deleteSavedLocation(locationId)
        }
    }

}