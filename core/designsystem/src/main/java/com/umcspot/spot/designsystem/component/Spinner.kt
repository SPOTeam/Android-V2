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

@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    speed: Float = 1f,
    isPlaying: Boolean = true,
    iterations: Int = LottieConstants.IterateForever,
    strokeColor: Color? = null,
    contentDescription: String? = "로딩 중"
) {
    // res/raw/spinner.lottie
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.spinner)
    )

    // 색상 오버라이드: 로티 내 "Stroke 1"에 적용
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
