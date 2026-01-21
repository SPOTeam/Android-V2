package com.umcspot.spot.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun SpotSpinner(
    modifier: Modifier = Modifier,
    size: Dp = screenWidthDp(24.dp),
    speed: Float = 1f,
    isPlaying: Boolean = true,
    iterations: Int = LottieConstants.IterateForever,
    strokeColor: Color? = null,
    contentDescription: String? = "로딩 중"
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.spinner)
    )

    val dynamicProps = if (strokeColor != null) {
        rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                property = LottieProperty.STROKE_COLOR,
                value = strokeColor.toArgb(),
                keyPath = arrayOf("**", "Stroke 1")
            )
        )
    } else {
        null
    }

    LottieAnimation(
        composition = composition,
        iterations = iterations,
        isPlaying = isPlaying,
        speed = speed,
        dynamicProperties = dynamicProps,
        modifier = modifier
            .size(size)
            .semantics {
                if (contentDescription != null) this.contentDescription = contentDescription
            }
    )
}
