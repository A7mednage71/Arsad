package com.example.arsad.presentation.alerts.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.presentation.alerts.model.AlertType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSave: (fromDate: String, fromTime: String, toDate: String, toTime: String, alertType: AlertType) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    var selectedAlertType by remember { mutableStateOf(AlertType.NOTIFICATION) }
    val todayMillis = System.currentTimeMillis()

    // From date/time state
    var fromDateMillis by remember { mutableStateOf(todayMillis) }
    var fromHour by remember { mutableStateOf(8) }
    var fromMinute by remember { mutableStateOf(0) }

    // To date/time state
    var toDateMillis by remember { mutableStateOf(todayMillis) }
    var toHour by remember { mutableStateOf(20) }
    var toMinute by remember { mutableStateOf(0) }

    // Dialog visibility
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showFromTimePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    var showToTimePicker by remember { mutableStateOf(false) }

    val fromDateDisplay = fromDateMillis.toDisplayDate()
    val fromTimeDisplay = formatTime(fromHour, fromMinute)
    val toDateDisplay = toDateMillis.toDisplayDate()
    val toTimeDisplay = formatTime(toHour, toMinute)

    // ── Date Picker dialogs ──
    if (showFromDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = fromDateMillis)
        DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { fromDateMillis = it }
                    showFromDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showFromDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = state) }
    }

    if (showToDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = toDateMillis)
        DatePickerDialog(
            onDismissRequest = { showToDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { toDateMillis = it }
                    showToDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showToDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = state) }
    }

    // ── Time Picker dialogs ──
    if (showFromTimePicker) {
        val state = rememberTimePickerState(
            initialHour = fromHour,
            initialMinute = fromMinute,
            is24Hour = false
        )
        TimePickerDialog(
            onDismiss = { showFromTimePicker = false },
            onConfirm = {
                fromHour = state.hour
                fromMinute = state.minute
                showFromTimePicker = false
            }
        ) { TimePicker(state = state) }
    }

    if (showToTimePicker) {
        val state = rememberTimePickerState(
            initialHour = toHour,
            initialMinute = toMinute,
            is24Hour = false
        )
        TimePickerDialog(
            onDismiss = { showToTimePicker = false },
            onConfirm = {
                toHour = state.hour
                toMinute = state.minute
                showToTimePicker = false
            }
        ) { TimePicker(state = state) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.onSurfaceVariant.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.alert_sheet_title),
                style = typography.titleLarge,
                color = colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Set the duration and alert type",
                style = typography.bodySmall,
                color = colors.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "DURATION", style = typography.labelSmall, color = colors.primary)
            Spacer(modifier = Modifier.height(12.dp))

            DateTimePickerRow(
                label = stringResource(R.string.alert_sheet_from),
                date = fromDateDisplay,
                time = fromTimeDisplay,
                onDateClick = { showFromDatePicker = true },
                onTimeClick = { showFromTimePicker = true }
            )
            Spacer(modifier = Modifier.height(10.dp))
            DateTimePickerRow(
                label = stringResource(R.string.alert_sheet_to),
                date = toDateDisplay,
                time = toTimeDisplay,
                onDateClick = { showToDatePicker = true },
                onTimeClick = { showToTimePicker = true }
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(20.dp))

            // Alert type section
            Text(text = "ALERT TYPE", style = typography.labelSmall, color = colors.primary)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AlertTypeChip(
                    label = stringResource(R.string.alert_type_notification),
                    iconRes = R.drawable.ic_notification_bold,
                    isSelected = selectedAlertType == AlertType.NOTIFICATION,
                    onClick = { selectedAlertType = AlertType.NOTIFICATION },
                    modifier = Modifier.weight(1f)
                )
                AlertTypeChip(
                    label = stringResource(R.string.alert_type_alarm),
                    iconRes = R.drawable.ic_alarm_sound,
                    isSelected = selectedAlertType == AlertType.ALARM,
                    onClick = { selectedAlertType = AlertType.ALARM },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.onSurface.copy(
                            alpha = 0.7f
                        )
                    ),
                    border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = stringResource(R.string.alert_sheet_cancel),
                        style = typography.labelLarge
                    )
                }
                Button(
                    onClick = {
                        onSave(
                            fromDateDisplay,
                            fromTimeDisplay,
                            toDateDisplay,
                            toTimeDisplay,
                            selectedAlertType
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.alert_sheet_save),
                        style = typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
