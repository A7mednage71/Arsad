package com.example.arsad.presentation.settings.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.presentation.settings.viewModel.AppTheme

@Composable
fun ThemeSection(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    SettingsSectionHeader(
        icon = R.drawable.ic_setting_light,
        title = stringResource(R.string.settings_section_theme)
    )
    Spacer(modifier = Modifier.height(12.dp))
    SettingsCard {
        SegmentedOptionRow(
            label = stringResource(R.string.settings_theme),
            options = listOf(
                stringResource(R.string.settings_theme_light),
                stringResource(R.string.settings_theme_dark)
            ),
            selectedIndex = when (selectedTheme) {
                AppTheme.LIGHT -> 0
                AppTheme.DARK -> 1
            },
            onOptionSelected = { index -> onThemeSelected(AppTheme.entries[index]) }
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}

