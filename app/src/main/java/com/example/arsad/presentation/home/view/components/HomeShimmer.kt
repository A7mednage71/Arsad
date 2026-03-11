package com.example.arsad.presentation.home.view.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
private fun shimmerBrush(): Brush {
    val baseColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
    val highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_x"
    )

    return Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(translateAnim - 400f, 0f),
        end = Offset(translateAnim, 0f)
    )
}


@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    radius: Dp = 12.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .background(shimmerBrush())
    )
}

@Composable
fun ShimmerCircle(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(shimmerBrush())
    )
}


@Composable
fun HomeShimmerLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // Location name
        ShimmerBox(
            modifier = Modifier
                .width(160.dp)
                .height(28.dp), radius = 8.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Date
        ShimmerBox(
            modifier = Modifier
                .width(110.dp)
                .height(18.dp), radius = 6.dp
        )
        Spacer(modifier = Modifier.height(28.dp))

        // Weather icon
        ShimmerCircle(size = 110.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Temperature big text
        ShimmerBox(
            modifier = Modifier
                .width(140.dp)
                .height(72.dp), radius = 16.dp
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Description
        ShimmerBox(
            modifier = Modifier
                .width(100.dp)
                .height(20.dp), radius = 6.dp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Feels like
        ShimmerBox(
            modifier = Modifier
                .width(130.dp)
                .height(16.dp), radius = 6.dp
        )
        Spacer(modifier = Modifier.height(28.dp))

        // Section title
        ShimmerBox(
            modifier = Modifier
                .width(120.dp)
                .height(20.dp)
                .align(Alignment.Start),
            radius = 6.dp
        )
        Spacer(modifier = Modifier.height(14.dp))

        // Details grid — 2x2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.15f), radius = 20.dp
            )
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.15f), radius = 20.dp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.15f), radius = 20.dp
            )
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.15f), radius = 20.dp
            )
        }
        Spacer(modifier = Modifier.height(28.dp))

        // Hourly section title
        ShimmerBox(
            modifier = Modifier
                .width(140.dp)
                .height(20.dp)
                .align(Alignment.Start),
            radius = 6.dp
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Hourly cards row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(6) {
                ShimmerBox(
                    modifier = Modifier
                        .width(80.dp)
                        .height(120.dp), radius = 20.dp
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))

        // 5-day section title
        ShimmerBox(
            modifier = Modifier
                .width(130.dp)
                .height(20.dp)
                .align(Alignment.Start),
            radius = 6.dp
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 5-day card
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp), radius = 24.dp
        )
    }
}

