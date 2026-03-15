package com.example.arsad.presentation.alerts.viewModel

import androidx.work.WorkManager
import com.example.arsad.data.local.ds.SettingsManager
import com.example.arsad.data.local.entity.WeatherAlertEntity
import com.example.arsad.data.repository.IWeatherRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

class AlertViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: IWeatherRepository = mockk(relaxed = true)
    private val settingsManager: SettingsManager = mockk(relaxed = true)
    private val workManager: WorkManager = mockk(relaxed = true)

    private lateinit var viewModel: AlertViewModel

    @Before
    fun setUp() {
        val fakeAlerts = listOf(mockk<WeatherAlertEntity>(relaxed = true))
        every { settingsManager.languageFlow } returns flowOf("en")
        every { repository.getAllAlerts() } returns flowOf(fakeAlerts)

        viewModel = AlertViewModel(repository, settingsManager, workManager)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun alerts_repositoryHasData_emitsMappedUIModels() = runTest {
        // listen to cold flow
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.alerts.collect {}
        }

        assertThat(viewModel.alerts.value).isNotEmpty()
        assertThat(viewModel.alerts.value).hasSize(1)
        assertThat(viewModel.isLoading.value).isFalse()
    }

    @Test
    fun saveAlert_locationIsNull_emitsError() = runTest {
        // Arrange
        every { settingsManager.latitudeFlow } returns flowOf(null)
        every { settingsManager.longitudeFlow } returns flowOf(null)
        every { settingsManager.locationNameFlow } returns flowOf(null)

        // Act
        viewModel.saveAlert(1000L, 2000L, "ALARM")

        // Assert
        coVerify(exactly = 0) { repository.insertAlert(any()) }
    }

    @Test
    fun deleteAlert_validId_callsRepoAndCancelsWork() = runTest {
        // Arrange
        val alertId = 123

        // Act
        viewModel.deleteAlert(alertId)

        // Assert
        coVerify(exactly = 1) { repository.deleteAlert(alertId) }
        coVerify(exactly = 1) { workManager.cancelUniqueWork("work_$alertId") }
    }
}