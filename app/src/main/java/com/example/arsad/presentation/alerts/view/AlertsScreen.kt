package com.example.arsad.presentation.alerts.view

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arsad.R
import com.example.arsad.presentation.alerts.view.components.AddAlertBottomSheet
import com.example.arsad.presentation.alerts.view.components.AlertItem
import com.example.arsad.presentation.alerts.view.components.AlertsEmptyState
import com.example.arsad.presentation.alerts.view.components.alertsListShimmer
import com.example.arsad.presentation.alerts.viewModel.AlertViewModel

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(modifier: Modifier = Modifier, viewModel: AlertViewModel) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val context = LocalContext.current


    val alerts by viewModel.alerts.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { messageResId ->
            val msg = context.getString(messageResId.toInt())
            Toast.makeText(
                context,
                "$msg ❌",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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

            if (isLoading) {
                alertsListShimmer()
            } else {
                items(alerts, key = { it.id }) { alert ->
                    AlertItem(
                        alert = alert,
                        onDelete = {
                            viewModel.deleteAlert(alert.id)
                        },
                        onToggle = { status ->
                            viewModel.toggleAlertStatus(alert.id, status)
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }

        if (alerts.isEmpty() && !isLoading) {
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
            onSave = { startTime, endTime, alertType ->
                viewModel.saveAlert(startTime, endTime, alertType.name)
                showSheet = false
            }
        )
    }
}

