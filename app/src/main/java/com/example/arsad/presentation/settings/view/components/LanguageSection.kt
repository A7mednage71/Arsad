package com.example.arsad.presentation.settings.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.presentation.settings.viewModel.AppLanguage
import com.example.arsad.presentation.settings.viewModel.SettingsUiState

@Composable
fun LanguageSection(
    state: SettingsUiState,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    SettingsSectionHeader(
        icon = R.drawable.ic_language,
        title = stringResource(R.string.settings_section_language)
    )
    Spacer(modifier = Modifier.height(12.dp))
    SettingsCard {
        SegmentedOptionRow(
            label = stringResource(R.string.settings_language),
            options = listOf(
                stringResource(R.string.settings_lang_english),
                stringResource(R.string.settings_lang_arabic)
            ),
            selectedIndex = when (state.language) {
                AppLanguage.ENGLISH -> 0
                AppLanguage.ARABIC -> 1
            },
            onOptionSelected = { index -> onLanguageSelected(AppLanguage.entries[index]) }
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}

