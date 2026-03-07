package com.example.arsad.presentation.settings.view.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsToggleRow(
    label: String,
    optionA: String,
    optionB: String,
    selectedA: Boolean,
    @DrawableRes iconResA: Int,
    @DrawableRes iconResB: Int,
    onSelectA: () -> Unit,
    onSelectB: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        Text(
            text = label,
            style = typography.labelMedium,
            color = colors.onSurface.copy(alpha = 0.75f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colors.background.copy(alpha = 0.3f)),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            ToggleChip(
                label = optionA,
                iconRes = iconResA,
                isSelected = selectedA,
                onClick = onSelectA,
                modifier = Modifier.weight(1f)
            )
            ToggleChip(
                label = optionB,
                iconRes = iconResB,
                isSelected = !selectedA,
                onClick = onSelectB,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ToggleChip(
    label: String,
    @DrawableRes iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else Color.Transparent,
        label = "chipBg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) colors.onPrimary else colors.onSurface.copy(alpha = 0.65f),
        label = "chipContent"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = typography.labelMedium,
            color = contentColor
        )
    }
}

