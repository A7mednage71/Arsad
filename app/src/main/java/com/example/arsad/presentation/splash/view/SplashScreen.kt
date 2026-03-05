package com.example.arsad.presentation.splash.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.arsad.R
import com.example.arsad.ui.theme.AppStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 4000L
) {
    val colors = MaterialTheme.colorScheme
    // this get lottie from raw and store it in memory to ignore multi Recomposition
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_lottie_2))

    val contentAlpha = remember { Animatable(0f) }
    val contentScale = remember { Animatable(0.95f) }

    LaunchedEffect(Unit) {
        launch { contentAlpha.animateTo(1f, tween(1000)) }
        launch { contentScale.animateTo(1f, tween(1000)) }
        delay(durationMillis)
        onFinished()
    }

    Box(modifier = modifier.fillMaxSize()) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colors.background.copy(alpha = 0.7f),
                            colors.surfaceVariant.copy(alpha = 0.85f),
                            colors.background
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(contentScale.value)
                .alpha(contentAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.height(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.splash_title).uppercase(),
                style = AppStyle.headlineLarge,
                color = colors.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.splash_subtitle),
                style = AppStyle.bodyLargeCenter,
                color = colors.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 48.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(160.dp)
                    .height(4.dp)
                    .clip(CircleShape),
                color = colors.primary,
                trackColor = colors.surfaceVariant.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.splash_loading),
                style = AppStyle.labelMedium,
                color = colors.onSurfaceVariant
            )
        }
        Text(
            text = "V 1.0",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            style = AppStyle.labelExtraSmall,
            color = colors.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}