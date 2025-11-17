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

// SpotColors/SpotDayColors는 기존 정의 사용
private val LocalSpotColors =
    staticCompositionLocalOf<SpotColors> { error("No colors provided") }

// 여기서 SpotTypography는 우리가 아래 Typography.kt에서 정의한 타입을 사용
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
    // copy()/update() 패턴으로 CompositionLocal 내 객체의 참조 안정성 유지
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
    // 디폴트 타이포 세트 팩토리 (아래 Typography.kt에서 제공)
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
            // M3 컴포넌트도 네 타이포 맵핑을 쓰도록 연결
            typography = AppTypography,
            content = content
        )
    }
}
