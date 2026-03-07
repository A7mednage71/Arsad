package com.example.arsad.presentation.alerts.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
internal fun AlertTypeChip(
    label: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary.copy(alpha = 0.15f)
        else colors.surfaceVariant.copy(alpha = 0.4f),
        label = "alertTypeBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary.copy(alpha = 0.6f)
        else Color.White.copy(alpha = 0.08f),
        label = "alertTypeBorder"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary
        else colors.onSurface.copy(alpha = 0.55f),
        label = "alertTypeContent"
    )

    Surface(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = label,
                style = typography.labelMedium,
                color = contentColor
            )
        }
    }
}


