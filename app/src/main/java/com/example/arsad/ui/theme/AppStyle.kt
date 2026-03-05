package com.example.arsad.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Immutable
object AppStyle {

    // Display Styles
    val displayLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 72.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onBackground
        )

    val displayMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 2.sp
        )

    val displaySmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )

    val displaySmallEnd: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End
        )

    // Headline Styles
    val headlineLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

    val headlineMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

    val headlineSmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

    // Title Styles
    val titleLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    val titleMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

    val titleSmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

    // Body Text Styles
    val bodyLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 24.sp
        )

    val bodyLargeCenter: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

    val bodyMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 20.sp
        )

    val bodyMediumSecondary: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )

    val bodySmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 16.sp
        )

    // Label Styles -
    val labelLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

    val labelMedium: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    val labelSmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    val labelExtraSmall: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
}


// @Composable: Marks the function as a UI building block,
// allowing it to call other composables and access theme data.

// @ReadOnlyComposable: An optimization that skips recomposition tracking
// for functions that only read values and don't emit UI nodes.
