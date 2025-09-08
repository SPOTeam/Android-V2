package com.example.core.ui.shapes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.B400
import com.example.core.ui.theme.G500
import com.example.core.ui.theme.R500
import com.example.core.ui.theme.White
import com.example.core.ui.theme.Y400

object SpotShapes {

    // ===== Shape 정의 (모서리) =====
    val Hard: Shape  = RoundedCornerShape(9.dp) // 20pt
    val Soft: Shape  = RoundedCornerShape(14.dp) // 32pt
    val Round: Shape = RoundedCornerShape(18.dp) // 42pt

    val SoftLeft: Shape  = RoundedCornerShape(
        topStart = 14.dp, topEnd = 0.dp, bottomStart = 14.dp, bottomEnd = 0.dp
    )
    val SoftRight: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 14.dp, bottomStart = 0.dp, bottomEnd = 14.dp
    )
    val RoundLeft: Shape  = RoundedCornerShape(
        topStart = 18.dp, topEnd = 0.dp, bottomStart = 18.dp, bottomEnd = 0.dp,
    )
    val RoundRight: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 18.dp, bottomStart = 0.dp, bottomEnd = 18.dp
    )
}

@Composable
fun ShapeBox(
    width: Dp,
    height: Dp,
    shape: Shape,
    color: Color = Color.White,
    borderWidth: Dp = 0.dp,
    borderColor: Color? = Color.Transparent,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}

) {
    Box(
        modifier = modifier
            .background(color, shape)
            .then(
                if (borderColor != null && borderWidth > 0.dp) {
                    Modifier.border(borderWidth, borderColor, shape)
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}

@Composable
fun ShapeBox(
    shape: Shape,
    color: Color = Color.White,
    borderWidth: Dp = 0.dp,
    borderColor: Color? = Color.Transparent,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}

) {
    Box(
        modifier = modifier
            .background(color, shape)
            .then(
                if (borderColor != null && borderWidth > 0.dp) {
                    Modifier.border(borderWidth, borderColor, shape)
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}

@Composable
fun StateCardActive(
    width: Dp,
    height: Dp,
    shape: Shape = SpotShapes.Hard,
    color: Color = White,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier
) = ShapeBox(
    width = width,
    height = height,
    shape = shape,
    color = color,
    borderWidth = borderWidth,
    borderColor = G500,
    modifier = modifier
)

@Composable
fun StateCardSuccess(
    width: Dp,
    height: Dp,
    shape: Shape = SpotShapes.Hard,
    color: Color = White,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier
) = ShapeBox(
    width = width,
    height = height,
    shape = shape,
    color = color,
    borderWidth = borderWidth,
    borderColor = B400,
    modifier = modifier
)

@Composable
fun StateCardError(
    width: Dp,
    height: Dp,
    shape: Shape = SpotShapes.Hard,
    color: Color = White,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier
) = ShapeBox(
    width = width,
    height = height,
    shape = shape,
    color = color,
    borderWidth = borderWidth,
    borderColor = R500,
    modifier = modifier
)

@Composable
fun StateCardWarning(
    width: Dp,
    height: Dp,
    shape: Shape = SpotShapes.Hard,
    color: Color = White,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier
) = ShapeBox(
    width = width,
    height = height,
    shape = shape,
    color = color,
    borderWidth = borderWidth,
    borderColor = Y400,
    modifier = modifier
)


