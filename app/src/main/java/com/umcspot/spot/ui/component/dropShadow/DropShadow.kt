package com.umcspot.spot.ui.component.dropShadow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.umcspot.spot.ui.component.shape.SpotShapes
import com.umcspot.spot.ui.theme.B500
import com.umcspot.spot.ui.theme.Black
import com.umcspot.spot.ui.theme.BlueGradient
import com.umcspot.spot.ui.theme.G100
import com.umcspot.spot.ui.theme.GrayGradient
import com.umcspot.spot.ui.theme.R500
import com.umcspot.spot.ui.theme.White

/**
 * Shape 전체 윤곽에 맞춰 그림자를 그려주는 DropShadow Modifier.
 *
 * @param x        그림자 X 오프셋
 * @param y        그림자 Y 오프셋
 * @param blur     그림자 블러 반경
 * @param opacity  그림자 불투명도 (0f ~ 1f)
 * @param shape    그림자를 적용할 모양(SpotShapes.* 등)
 * @param shadowColor  그림자 색상 (기본 #1E1E1E)
 */
fun Modifier.dropShadow(
    x: Dp,
    y: Dp,
    blur: Dp,
    opacity: Float,
    shape: Shape,
    shadowColor: Color = Black
): Modifier = this.then(
    Modifier.drawBehind {
        // 현재 레이아웃 크기에 맞춰 Shape → Outline 생성
        val outline: Outline = shape.createOutline(
            size = Size(size.width, size.height),
            layoutDirection = LayoutDirection.Ltr,
            density = this
        )

        // Outline → Path 변환
        val path = Path().apply {
            when (outline) {
                is Outline.Rectangle -> addRect(outline.rect)
                is Outline.Rounded -> addRoundRect(outline.roundRect)
                is Outline.Generic -> addPath(outline.path)
            }
        }

        // 안드로이드 Paint 로 섀도우 그리기
        val fwPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            color = android.graphics.Color.TRANSPARENT // 내부는 투명, 섀도우만
            setShadowLayer(
                /* radius   = */ blur.toPx(),
                /* dx       = */ x.toPx(),
                /* dy       = */ y.toPx(),
                /* shadow   = */ shadowColor.copy(alpha = opacity).toArgb()
            )
        }

        drawIntoCanvas { canvas ->
            // Path 자체에 그림자 계층을 적용해 섀도우 렌더링
            canvas.nativeCanvas.drawPath(path.asAndroidPath(), fwPaint)
        }
    }
)

@Composable
fun ShadowedShapeBox(
    width: Dp,
    height: Dp,
    shape: Shape,
    shadowX: Dp,
    shadowY: Dp,
    shadowBlur: Dp,
    shadowOpacity: Float,
    shadowColor: Color = Black,
    brush: Brush? = null,
    backgroundColor: Color = White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width, height)
            .dropShadow(
                x = shadowX,
                y = shadowY,
                blur = shadowBlur,
                opacity = shadowOpacity,
                shape = shape,
                shadowColor = shadowColor
            )
            .let { m ->
                if (brush != null) m.background(brush, shape)
                else m.background(backgroundColor, shape)
            }
            .then (
                if (borderWidth > 0.dp)
                    Modifier.border(borderWidth, borderColor, shape)
                else
                    Modifier
            )
    )
}

@Composable
fun DS100(
    width: Dp,
    height: Dp,
    shape: Shape,
    brush: Brush? = null,
    backgroundColor: Color = White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent
) = ShadowedShapeBox(
    width = width,
    height = height,
    shape = shape,
    shadowX = 0.dp,
    shadowY = 0.dp,
    shadowBlur = 12.dp,
    shadowOpacity = 0.16f,
    brush = brush,
    backgroundColor = backgroundColor,
    borderWidth = borderWidth,
    borderColor = borderColor
)

@Composable
fun DS100_Y(
    width: Dp,
    height: Dp,
    shape: Shape,
    brush: Brush? = null,
    backgroundColor: Color = White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent
) = ShadowedShapeBox(
    width = width,
    height = height,
    shape = shape,
    shadowX = 0.dp,
    shadowY = 1.dp,
    shadowBlur = 12.dp,
    shadowOpacity = 0.16f,
    brush = brush,
    backgroundColor = backgroundColor,
    borderWidth = borderWidth,
    borderColor = borderColor
)

@Composable
fun DS200(
    width: Dp,
    height: Dp,
    shape: Shape,
    brush: Brush? = null,
    backgroundColor: Color = White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent
) = ShadowedShapeBox(
    width = width,
    height = height,
    shape = shape,
    shadowX = 0.dp,
    shadowY = 0.dp,
    shadowBlur = 12.dp,
    shadowOpacity = 0.24f,
    brush = brush,
    backgroundColor = backgroundColor,
    borderWidth = borderWidth,
    borderColor = borderColor
)

@Composable
fun DS300(
    width: Dp,
    height: Dp,
    shape: Shape,
    brush: Brush? = null,
    backgroundColor: Color = White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent
) = ShadowedShapeBox(
    width = width,
    height = height,
    shape = shape,
    shadowX = 0.dp,
    shadowY = 0.dp,
    shadowBlur = 12.dp,
    shadowOpacity = 0.32f,
    brush = brush,
    backgroundColor = backgroundColor,
    borderWidth = borderWidth,
    borderColor = borderColor
)

@Composable
fun DS400(
    width: Dp,
    height: Dp,
    shape: Shape,
    brush: Brush? = null,
    backgroundColor: Color = White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent
) = ShadowedShapeBox(
    width = width,
    height = height,
    shape = shape,
    shadowX = 0.dp,
    shadowY = 0.dp,
    shadowBlur = 20.dp,
    shadowOpacity = 0.32f,
    brush = brush,
    backgroundColor = backgroundColor,
    borderWidth = borderWidth,
    borderColor = borderColor
)
