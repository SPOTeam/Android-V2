package com.umcspot.spot.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umcspot.spot.ui.theme.B500
import com.umcspot.spot.ui.theme.Black
import com.umcspot.spot.ui.theme.G100
import com.umcspot.spot.ui.theme.White

/* ============ 커스텀 Thumb (원형 + 테두리) ============ */
@Composable
private fun SpotThumb(
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
    diameter: Dp,
    fill: Color,
    borderWidth: Dp,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(diameter)
            .background(fill, CircleShape)
            .border(borderWidth, borderColor, CircleShape)
    )
}

/* ============ 범위 슬라이더 (두 개 손잡이) ============ */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotRangeSlider(
    values: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    thumbSize: Dp = 22.dp,               // 썸 지름
    thumbBorderWidth: Dp = 1.dp,
    trackHeight: Dp = 8.dp,
    activeColor: Color = B500,
    inactiveColor: Color = G100,
    thumbFill: Color = White,
    onValueChangeFinished: (() -> Unit)? = null
) {
    val startIS = remember { MutableInteractionSource() }
    val endIS = remember { MutableInteractionSource() }

    val colors = SliderDefaults.colors(
        thumbColor = thumbFill,                // 내부적으로 쓰이는 값(리플 등)
        activeTrackColor = activeColor,
        inactiveTrackColor = inactiveColor,
        activeTickColor = Color.Transparent,
        inactiveTickColor = Color.Transparent,
        disabledActiveTrackColor = activeColor.copy(alpha = 0.4f),
        disabledInactiveTrackColor = inactiveColor.copy(alpha = 0.4f)
    )

    RangeSlider(
        value = values,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors,
        startInteractionSource = startIS,
        endInteractionSource = endIS,
        // 슬롯 시그니처: (state) -> Unit
        startThumb = { _ ->
            SpotThumb(
                interactionSource = startIS,
                enabled = enabled,
                diameter = thumbSize,
                fill = thumbFill,
                borderWidth = thumbBorderWidth,
                borderColor = activeColor
            )
        },
        endThumb = { _ ->
            SpotThumb(
                interactionSource = endIS,
                enabled = enabled,
                diameter = thumbSize,
                fill = thumbFill,
                borderWidth = thumbBorderWidth,
                borderColor = activeColor
            )
        },
        track = { state ->
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
            ) {
                val radius = size.height / 2f

                // 전체(비활성) 트랙
                drawRoundRect(
                    color = inactiveColor,
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(radius, radius)
                )

                // 활성 구간
                val startX = (state.activeRangeStart.coerceIn(0f, 1f)) * size.width
                val endX = (state.activeRangeEnd.coerceIn(0f, 1f)) * size.width
                val width = (endX - startX).coerceAtLeast(0f)

                drawRoundRect(
                    color = activeColor,
                    topLeft = Offset(startX, 0f),
                    size = Size(width, size.height),
                    cornerRadius = CornerRadius(radius, radius)
                )
            }
        }
    )
}

/* ===================== Previews ===================== */

@Preview(showBackground = true, name = "Full (0..1)")
@Composable
private fun Preview_Range_Full() {
    var range by remember { mutableStateOf(0f..1f) }
    SpotRangeSlider(
        values = range,
        onValueChange = { range = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
