package com.example.arsad.presentation.settings.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.presentation.settings.viewModel.SettingsUiState
import com.example.arsad.presentation.settings.viewModel.TemperatureUnit
import com.example.arsad.presentation.settings.viewModel.WindSpeedUnit

@Composable
fun UnitsSection(
    state: SettingsUiState,
    onTemperatureUnitSelected: (TemperatureUnit) -> Unit,
    onWindSpeedUnitSelected: (WindSpeedUnit) -> Unit
) {
    SettingsSectionHeader(
        icon = R.drawable.ic_temperature,
        title = stringResource(R.string.settings_section_units)
    )
    Spacer(modifier = Modifier.height(12.dp))
    SettingsCard {
        SegmentedOptionRow(
            label = stringResource(R.string.settings_temp_unit),
            options = listOf(
                stringResource(R.string.settings_temp_celsius),
                stringResource(R.string.settings_temp_fahrenheit),
                stringResource(R.string.settings_temp_kelvin)
            ),
            selectedIndex = when (state.temperatureUnit) {
                TemperatureUnit.CELSIUS -> 0
                TemperatureUnit.FAHRENHEIT -> 1
                TemperatureUnit.KELVIN -> 2
            },
            onOptionSelected = { index -> onTemperatureUnitSelected(TemperatureUnit.entries[index]) }
        )
        SegmentedOptionRow(
            label = stringResource(R.string.settings_wind_unit),
            options = listOf(
                stringResource(R.string.settings_wind_ms),
                stringResource(R.string.settings_wind_mph)
            ),
            selectedIndex = when (state.windSpeedUnit) {
                WindSpeedUnit.METER_PER_SEC -> 0
                WindSpeedUnit.MILE_PER_HOUR -> 1
            },
            onOptionSelected = { index -> onWindSpeedUnitSelected(WindSpeedUnit.entries[index]) }
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}

