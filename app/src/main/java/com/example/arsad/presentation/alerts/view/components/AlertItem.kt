package com.example.arsad.presentation.alerts.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arsad.R
import com.example.arsad.data.models.AlertType
import com.example.arsad.data.models.WeatherAlertModel
import com.example.arsad.presentation.components.WarningDialog
import com.example.arsad.ui.theme.DeleteRed
import com.example.arsad.ui.theme.SunYellow
import com.example.arsad.ui.theme.Turquoise
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertItem(
    alert: WeatherAlertModel,
    onDelete: () -> Unit,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val currentTime = System.currentTimeMillis()
    val isExpired = currentTime > alert.endTimeMillis

    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val isEffectivelyActive = alert.isActive && !isExpired

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showDeleteDialog = true
            }
            false
        },
        positionalThreshold = { distance -> distance * 0.5f }
    )


    if (showDeleteDialog) {
        WarningDialog(
            title = stringResource(R.string.delete_dialog_title),
            message = stringResource(R.string.delete_dialog_message),
            confirmText = stringResource(R.string.delete_confirm),
            dismissText = stringResource(R.string.delete_cancel),
            iconRes = R.drawable.ic_delete_2,
            confirmButtonColor = DeleteRed,
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            onDismiss = {
                scope.launch {
                    dismissState.reset()
                }
                showDeleteDialog = false
            }
        )
    }

    val isAlarm = alert.alertType == AlertType.ALARM
    val themeColor = if (isAlarm) SunYellow else Turquoise

    val cardBackgroundColor = if (isEffectivelyActive) {
        colors.surface
    } else {
        colors.surfaceVariant.copy(alpha = 1f)
    }

    val statusColor =
        if (isEffectivelyActive) themeColor else colors.onSurfaceVariant.copy(alpha = 0.6f)
    val contentAlpha = if (isEffectivelyActive) 1f else 0.5f

    val statusLabel = when {
        isExpired -> stringResource(R.string.alert_expired)
        !alert.isActive -> stringResource(R.string.alert_Inactive)
        else -> stringResource(R.string.alert_active)
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp)
                    .background(DeleteRed, RoundedCornerShape(24.dp))
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete_2),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(24.dp),
            color = cardBackgroundColor,
            border = BorderStroke(
                1.dp,
                Color.White.copy(alpha = if (isEffectivelyActive) 0.08f else 0.02f)
            ),
            shadowElevation = if (isEffectivelyActive) 2.dp else 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = statusColor.copy(alpha = 0.15f * contentAlpha),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(if (isAlarm) R.drawable.ic_alarm_sound else R.drawable.ic_notification_bold),
                            contentDescription = null,
                            tint = statusColor.copy(alpha = contentAlpha),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = alert.locationName.ifEmpty { "Current Location" },
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        ),
                        color = colors.onSurface.copy(alpha = if (isEffectivelyActive) 1f else 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TimeRow(
                        label = stringResource(R.string.alert_sheet_from),
                        date = alert.fromDate,
                        time = alert.fromTime,
                        isHighlighted = isEffectivelyActive
                    )
                    TimeRow(
                        label = stringResource(R.string.alert_sheet_to),
                        date = alert.toDate,
                        time = alert.toTime,
                        isHighlighted = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(statusColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = statusLabel,
                            style = typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = statusColor
                        )
                    }
                }

                if (!isExpired) {
                    Switch(
                        checked = alert.isActive,
                        onCheckedChange = { isActive ->
                            onToggle(isActive)
                        },
                        colors = SwitchDefaults.colors(
                            // (On)
                            checkedThumbColor = Color.White,
                            checkedTrackColor = themeColor,

                            // (Off)
                            uncheckedThumbColor = colors.onSurface.copy(alpha = 0.4f),
                            uncheckedTrackColor = colors.onSurface.copy(alpha = 0.1f),
                            uncheckedBorderColor = colors.onSurface.copy(alpha = 0.2f),

                            // Disabled
                            disabledUncheckedTrackColor = colors.onSurface.copy(alpha = 0.05f),
                            disabledCheckedTrackColor = themeColor.copy(alpha = 0.1f)
                        ),
                        thumbContent = if (alert.isActive) {
                            {
                                Icon(
                                    painter = painterResource(if (isAlarm) R.drawable.ic_alarm_sound else R.drawable.ic_notification_bold),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                    tint = Color.White
                                )
                            }
                        } else null
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeRow(label: String, date: String, time: String, isHighlighted: Boolean) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val contentAlpha = if (isHighlighted) 1f else 0.65f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = colors.primary.copy(alpha = if (isHighlighted) 0.9f else 0.5f),
            modifier = Modifier.width(32.dp)
        )
        Icon(
            painter = painterResource(R.drawable.ic_clock),
            contentDescription = null,
            tint = colors.onSurface.copy(alpha = contentAlpha * 0.7f),
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = "$date  •  $time",
            style = typography.labelMedium.copy(
                fontWeight = if (isHighlighted) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = colors.onSurface.copy(alpha = contentAlpha)
        )
    }
}