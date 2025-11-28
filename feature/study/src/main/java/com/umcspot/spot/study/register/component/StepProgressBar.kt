package com.umcspot.spot.study.register.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.G100
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun StepProgressBar(
    currentStep: Int, 
    totalSteps: Int = 4,
    activeColor: Color = SpotTheme.colors.B400,
    inactiveColor: Color = SpotTheme.colors.G100
) {
    val targetProgress = (currentStep.toFloat() / totalSteps.toFloat()).coerceIn(0.0f, 1.0f)
    
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        label = "ProgressBarAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(3.dp))
            .clip(RoundedCornerShape(12.dp)) 
            .background(inactiveColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress) 
                .height(screenHeightDp(3.dp))
                .background(activeColor)
        )
    }
}