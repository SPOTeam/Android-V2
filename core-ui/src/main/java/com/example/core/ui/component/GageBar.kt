package com.example.core.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.G100

/**
 * 게이지 바 (Progress / Gauge)
 *
 * @param value          0f..1f 사이 진행값
 * @param height         바 높이
 * @param trackColor     트랙(배경) 색
 * @param fillColor      진행 색 (brush가 null일 때 사용)
 * @param brush          진행 그라데이션 (옵션)
 * @param shape          모서리 (기본: 원형 pill)
 * @param animate        값 변경 시 부드럽게 애니메이션
 * @param borderWidth    테두리 두께 (0dp면 없음)
 * @param borderColor    테두리 색
 */
@Composable
fun GaugeBar(
    value: Float,
    modifier: Modifier = Modifier,
    height: Dp = 12.dp,
    trackColor: Color = G100,
    fillColor: Color = B500,
    shape: Shape = RoundedCornerShape(50), // 높이에 관계없이 pill
    animate: Boolean = true,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent
) {
    val clamped = value.coerceIn(0f, 1f)
    val animated by animateFloatAsState(targetValue = if (animate) clamped else clamped, label = "gauge")

    // 트랙
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(trackColor)
            .then(
                if (borderWidth > 0.dp) Modifier.border(borderWidth, borderColor, shape)
                else Modifier
            )
    ) {
        // 진행(필)
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = animated.coerceIn(0f, 1f))
                .height(height)
                .clip(shape)
                .background(fillColor)
        )
    }
}

/* =================== Previews =================== */

@Preview(showBackground = true, name = "15%")
@Composable
fun Preview_GaugeBar_15() {
    Column(modifier = Modifier.padding(16.dp)) {
        GaugeBar(
            value = 0.15f, // 퍼센트 조절 가능
            height = 12.dp,
            trackColor = G100,
            borderWidth = 0.dp
        )
    }
}

@Preview(showBackground = true, name = "75%")
@Composable
fun Preview_GaugeBar_75() {
    Column(modifier = Modifier.padding(16.dp)) {
        GaugeBar(
            value = 0.75f, // 퍼센트 조절 가능
            height = 12.dp,
            trackColor = G100,
            borderWidth = 0.dp
        )
    }
}


