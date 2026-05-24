package com.umcspot.spot.study.register.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.G100
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StepProgressBar(
    currentStep: Int,
    totalSteps: Int = 4,
    isCurrentStepValid: Boolean = false,
    activeColor: Color = SpotTheme.colors.B400,
    inactiveColor: Color = SpotTheme.colors.G100
) {
    val padding = screenWidthDp(17.dp)
    var fullWidthDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val effectiveStep = if (isCurrentStepValid) currentStep else currentStep - 1

    val filledWidth = if (fullWidthDp > 0.dp) {
        (padding + (fullWidthDp - padding * 2) * (effectiveStep.toFloat() / (totalSteps).toFloat()))
            .coerceIn(padding, fullWidthDp - padding)
    } else 0.dp

    val animatedWidth by animateDpAsState(
        targetValue = filledWidth,
        label = "ProgressBarAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(3.dp))
            .background(inactiveColor)
            .onGloballyPositioned { coordinates ->
                with(density) { fullWidthDp = coordinates.size.width.toDp() }
            }
    ) {
        Box(
            modifier = Modifier
                .width(animatedWidth)
                .height(screenHeightDp(3.dp))
                .background(activeColor)
        )
    }
}