package com.umcspot.spot.designsystem.theme


import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LocalSpotColors =
    staticCompositionLocalOf<SpotColors> { error("No colors provided") }
private val LocalSpotTypography =
    staticCompositionLocalOf<SpotTypography> { error("No typography provided") }

object SpotTheme {
    val colors: SpotColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSpotColors.current

    val typography: SpotTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalSpotTypography.current
}

@Composable
fun ProvideSpotColorsAndTypography(
    colors: SpotColors,
    typography: SpotTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }
    provideColors.update(colors)
    val provideTypography = remember { typography.copy() }
    provideTypography.update(typography)

    CompositionLocalProvider(
        LocalSpotColors provides provideColors,
        LocalSpotTypography provides provideTypography,
        content = content
    )
}

@Composable
fun SpotTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = SpotDayColors()
    val typography = SpotTypography()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }
    ProvideSpotColorsAndTypography(colors, typography) {
        MaterialTheme(
            content = content
        )
    }
}
