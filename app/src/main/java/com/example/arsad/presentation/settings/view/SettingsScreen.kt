package com.example.arsad.presentation.settings.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.arsad.R
import com.example.arsad.presentation.settings.view.components.LanguageSection
import com.example.arsad.presentation.settings.view.components.LocationSection
import com.example.arsad.presentation.settings.view.components.UnitsSection
import com.example.arsad.presentation.settings.viewModel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        // Header
        item {
            Spacer(modifier = Modifier.height(52.dp))
            Text(
                text = stringResource(R.string.settings_title),
                style = typography.headlineMedium,
                color = colors.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.settings_subtitle),
                style = typography.bodySmall,
                color = colors.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(28.dp))
        }

        // Units Section
        item {
            UnitsSection(
                state = state,
                onTemperatureUnitSelected = { viewModel.setTemperatureUnit(it) },
                onWindSpeedUnitSelected = { viewModel.setWindSpeedUnit(it) }
            )
        }

        // Language Section
        item {
            LanguageSection(
                state = state,
                onLanguageSelected = { viewModel.setLanguage(it) }
            )
        }

        // Location Section
        item {
            LocationSection(
                state = state,
                onLocationMethodSelected = { viewModel.setLocationMethod(it) }
            )
        }
    }
}
