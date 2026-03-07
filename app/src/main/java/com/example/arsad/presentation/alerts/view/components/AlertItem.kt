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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.arsad.R
import com.example.arsad.presentation.alerts.model.AlertType
import com.example.arsad.presentation.alerts.model.WeatherAlert
import com.example.arsad.ui.theme.DeleteRed
import com.example.arsad.ui.theme.SunYellow
import com.example.arsad.ui.theme.Turquoise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertItem(
    alert: WeatherAlert,
    onDelete: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
        }
    }

    val isAlarm = alert.alertType == AlertType.ALARM
    val themeColor = if (isAlarm) SunYellow else Turquoise
    val statusColor = if (alert.isActive) Turquoise else colors.onSurfaceVariant.copy(alpha = 0.4f)

    val statusLabel = if (alert.isActive)
        stringResource(R.string.alert_active)
    else
        stringResource(R.string.alert_expired)

    val typeLabel = if (isAlarm)
        stringResource(R.string.alert_type_alarm)
    else
        stringResource(R.string.alert_type_notification)

    val disActiveIcon = if (isAlarm)
        painterResource(R.drawable.ic_alarm_off)
    else
        painterResource(R.drawable.ic_notification_off)

    val typeIcon = if (isAlarm) R.drawable.ic_alarm_sound else R.drawable.ic_notification_bold

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
            color = colors.surface,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = themeColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(typeIcon),
                            contentDescription = null,
                            tint = themeColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TimeRow(
                        label = stringResource(R.string.alert_sheet_from),
                        date = alert.fromDate,
                        time = alert.fromTime,
                        isHighlight = alert.isActive
                    )
                    TimeRow(
                        label = stringResource(R.string.alert_sheet_to),
                        date = alert.toDate,
                        time = alert.toTime,
                        isHighlight = false
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(statusColor, CircleShape)
                            )
                            Text(
                                text = statusLabel,
                                style = typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = statusColor
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colors.onSurface.copy(alpha = 0.05f)
                        ) {
                            Text(
                                text = typeLabel,
                                style = typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = colors.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                if (alert.isActive) {
                    IconButton(
                        onClick = onStop,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = disActiveIcon,
                            contentDescription = stringResource(R.string.alert_stop),
                            tint = colors.onSurface.copy(alpha = 0.45f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeRow(label: String, date: String, time: String, isHighlight: Boolean) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val contentAlpha = if (isHighlight) 0.9f else 0.5f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = colors.primary.copy(alpha = if (isHighlight) 0.8f else 0.4f),
            modifier = Modifier.width(28.dp)
        )
        Icon(
            painter = painterResource(R.drawable.ic_clock),
            contentDescription = null,
            tint = colors.onSurface.copy(alpha = contentAlpha * 0.6f),
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = "$date  •  $time",
            style = typography.labelMedium.copy(fontWeight = if (isHighlight) FontWeight.Medium else FontWeight.Normal),
            color = colors.onSurface.copy(alpha = contentAlpha)
        )
    }
}