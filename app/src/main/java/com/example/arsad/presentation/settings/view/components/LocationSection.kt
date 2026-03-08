package com.example.arsad.presentation.settings.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.presentation.settings.viewModel.LocationMethod

@Composable
fun LocationSection(
    selectedLocationMethod: LocationMethod,
    onLocationMethodSelected: (LocationMethod) -> Unit
) {
    SettingsSectionHeader(
        icon = R.drawable.ic_location,
        title = stringResource(R.string.settings_section_location)
    )
    Spacer(modifier = Modifier.height(12.dp))
    SettingsCard {
        SettingsToggleRow(
            label = stringResource(R.string.settings_location_method),
            optionA = stringResource(R.string.settings_location_gps),
            optionB = stringResource(R.string.settings_location_map),
            selectedA = selectedLocationMethod == LocationMethod.GPS,
            iconResA = R.drawable.ic_pick_gps,
            iconResB = R.drawable.ic_map,
            onSelectA = { onLocationMethodSelected(LocationMethod.GPS) },
            onSelectB = { onLocationMethodSelected(LocationMethod.MAP) }
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}
