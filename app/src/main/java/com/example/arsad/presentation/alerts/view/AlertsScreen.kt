package com.example.arsad.presentation.alerts.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.presentation.alerts.model.AlertType
import com.example.arsad.presentation.alerts.model.WeatherAlert
import com.example.arsad.presentation.alerts.view.components.AddAlertBottomSheet
import com.example.arsad.presentation.alerts.view.components.AlertItem
import com.example.arsad.presentation.alerts.view.components.AlertsEmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val alerts = remember {
        mutableStateListOf(
            WeatherAlert(
                1,
                "08:00 AM",
                "06:00 PM",
                "Mar 7, 2026",
                "Mar 7, 2026",
                AlertType.NOTIFICATION,
                true
            ),
            WeatherAlert(
                2,
                "09:00 PM",
                "11:59 PM",
                "Mar 6, 2026",
                "Mar 6, 2026",
                AlertType.ALARM,
                false
            ), WeatherAlert(
                3,
                "08:00 AM",
                "06:00 PM",
                "Mar 7, 2026",
                "Mar 7, 2026",
                AlertType.NOTIFICATION,
                false
            ),
            WeatherAlert(
                4,
                "09:00 PM",
                "11:59 PM",
                "Mar 6, 2026",
                "Mar 6, 2026",
                AlertType.ALARM,
                true
            )
        )
    }

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.alerts_title),
                    style = typography.headlineMedium,
                    color = colors.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.alerts_subtitle),
                    style = typography.bodySmall,
                    color = colors.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            items(alerts, key = { it.id }) { alert ->
                AlertItem(
                    alert = alert,
                    onDelete = { alerts.remove(alert) },
                    onStop = {
                        val index = alerts.indexOf(alert)
                        if (index >= 0) alerts[index] = alert.copy(isActive = false)
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }

        if (alerts.isEmpty()) {
            AlertsEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }

        FloatingActionButton(
            onClick = { showSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 120.dp),
            containerColor = colors.primary,
            contentColor = colors.onPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.alerts_add)
            )
        }
    }

    // Bottom Sheet
    if (showSheet) {
        AddAlertBottomSheet(
            sheetState = sheetState,
            onDismiss = { showSheet = false },
            onSave = { fromDate, fromTime, toDate, toTime, alertType ->
                alerts.add(
                    WeatherAlert(
                        id = (alerts.maxOfOrNull { it.id } ?: 0) + 1,
                        fromTime = fromTime,
                        toTime = toTime,
                        fromDate = fromDate,
                        toDate = toDate,
                        alertType = alertType,
                        isActive = true
                    )
                )
                showSheet = false
            }
        )
    }
}

