package com.example.arsad.presentation.settings.view.components

import android.Manifest
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.presentation.settings.viewModel.LocationMethod
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationSection(
    selectedLocationMethod: LocationMethod,
    locationName: String?,
    onLocationMethodSelected: (LocationMethod) -> Unit,
    onGpsRequested: () -> Unit,
    onMapRequested: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    SettingsSectionHeader(
        icon = R.drawable.ic_location,
        title = stringResource(R.string.settings_section_location)
    )
    Spacer(modifier = Modifier.height(12.dp))

    SettingsCard {
        // GPS / Map toggle
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

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (selectedLocationMethod == LocationMethod.GPS) {
                    if (locationPermission.status.isGranted) onGpsRequested()
                    else locationPermission.launchPermissionRequest()
                } else {
                    onMapRequested()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
        ) {
            Icon(
                painter = painterResource(
                    id = if (selectedLocationMethod == LocationMethod.GPS) R.drawable.ic_pick_gps
                    else R.drawable.ic_map
                ),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = when (selectedLocationMethod) {
                    LocationMethod.GPS -> {
                        if (locationName != null) stringResource(R.string.settings_location_update_gps)
                        else stringResource(R.string.settings_location_get_gps)
                    }

                    LocationMethod.MAP -> stringResource(R.string.settings_location_update_map)
                },
                style = typography.labelLarge
            )
        }

        if (locationName != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = locationName,
                style = typography.labelSmall,
                color = colors.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}