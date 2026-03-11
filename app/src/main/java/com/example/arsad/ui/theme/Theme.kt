package com.example.arsad.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SunYellow,
    onPrimary = NightModeBackground,
    secondary = SkyBlue,
    tertiary = Turquoise,
    background = NightModeBackground,
    onBackground = TextOnDarkPrimary,
    surface = NightModeSurface,
    onSurface = TextOnDarkPrimary,
    surfaceVariant = NightModeSurfaceAlt,
    onSurfaceVariant = TextOnDarkSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = SunYellow,
    onPrimary = NightModeBackground,
    secondary = SkyBlue,
    tertiary = Turquoise,
    background = DayModeBackground,
    onBackground = TextOnLightPrimary,
    surface = DayModeSurface,
    onSurface = TextOnLightPrimary,
    surfaceVariant = DayModeSurfaceAlt,
    onSurfaceVariant = TextOnLightSecondary
)

@Composable
fun ArsadTheme(
    darkTheme: Boolean = true,
    // Keep a fixed palette unless explicitly enabled.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getAppTypography(),
        content = content
    )
}

object ArsadGradient {
    val screenBackground: Brush
        @Composable
        @ReadOnlyComposable
        get() {
            val isDark = MaterialTheme.colorScheme.background == NightModeBackground
            return if (isDark) {
                Brush.verticalGradient(listOf(GradientDarkTop, GradientDarkMid, GradientDarkBottom))
            } else {
                Brush.verticalGradient(
                    listOf(
                        GradientLightTop,
                        GradientLightMid,
                        GradientLightBottom
                    )
                )
            }
        }
}
