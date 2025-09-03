package com.umcspot.spot.ui.asset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SpotShapes {

    // ===== Shape 정의 (모서리) =====
    val Hard: Shape  = RoundedCornerShape(20.dp)
    val Soft: Shape  = RoundedCornerShape(32.dp)
    val Round: Shape = RoundedCornerShape(42.dp)

    val SoftLeft: Shape  = RoundedCornerShape(
        topStart = 12.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 0.dp
    )
    val SoftRight: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 12.dp
    )

    val RoundLeft: Shape  = RoundedCornerShape(
        topStart = 16.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 0.dp,
    )
    val RoundRight: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 16.dp
    )
}

// ===== 공통 Box Wrapper =====
@Composable
fun ShapeBox(
    width: Dp,
    height: Dp,
    shape: Shape,
    color: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width, height)
            .background(color, shape)
    )
}

/*
    사용 예시

    ShapeBox(
        width = 200.dp,
        height = 120.dp,
        shape = SpotShapes.Hard
    )

    or

    ShapeBox(
        width = 160.dp,
        height = 100.dp,
        shape = SpotShapes.SoftRight,
        color = Color.LightGray
    )
 */