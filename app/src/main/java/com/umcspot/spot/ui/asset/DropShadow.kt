package com.umcspot.spot.ui.asset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
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
    shadowColor: Color = Color(0xFF1E1E1E)
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
        val fwPaint = androidx.compose.ui.graphics.Paint().asFrameworkPaint().apply {
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

/**
 * 그림자 + 배경을 한 번에 그려주는 공용 컴포저블.
 *
 * @param width           박스 가로
 * @param height          박스 세로
 * @param shape           모양(둥근/비대칭 등)
 * @param shadowX         섀도 X 오프셋
 * @param shadowY         섀도 Y 오프셋
 * @param shadowBlur      섀도 블러
 * @param shadowOpacity   섀도 불투명도
 * @param shadowColor     섀도 색상
 * @param backgroundColor 내부 배경색
 * @param modifier        추가 Modifier
 */
@Composable
fun ShadowedShapeBox(
    width: Dp,
    height: Dp,
    shape: Shape,
    shadowX: Dp,
    shadowY: Dp,
    shadowBlur: Dp,
    shadowOpacity: Float,
    shadowColor: Color = Color(0xFF1E1E1E),
    backgroundColor: Color = Color.White,
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
            .background(backgroundColor, shape)
    )
}

/* ===== 섀도 프리셋 (기존 DS100~DS400와 동일 파라미터, 단 shape 추가) ===== */

@Composable
fun DS100(width: Dp, height: Dp, shape: Shape) =
    ShadowedShapeBox(width, height, shape, 0.dp, 0.dp, 12.dp, 0.16f)

@Composable
fun DS100_Y(width: Dp, height: Dp, shape: Shape) =
    ShadowedShapeBox(width, height, shape, 0.dp, 1.dp, 12.dp, 0.16f)

@Composable
fun DS200(width: Dp, height: Dp, shape: Shape) =
    ShadowedShapeBox(width, height, shape, 0.dp, 0.dp, 12.dp, 0.24f)

@Composable
fun DS300(width: Dp, height: Dp, shape: Shape) =
    ShadowedShapeBox(width, height, shape, 0.dp, 0.dp, 12.dp, 0.32f)

@Composable
fun DS400(width: Dp, height: Dp, shape: Shape) =
    ShadowedShapeBox(width, height, shape, 0.dp, 0.dp, 20.dp, 0.32f)


@Preview(name = "Hard", showBackground = true)
@Composable
fun Preview_DS100() {
    Box(
        modifier = Modifier.padding(10.dp) // 여백 추가
    ) {
        DS100(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.Hard
        )
    }
}

@Preview(name = "DS100_Y - Hard", showBackground = true)
@Composable
fun Preview_DS100_Y() {
    Box(
        modifier = Modifier.padding(10.dp) // 여백 추가
    ) {
        DS100_Y(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.Hard
        )
    }
}

@Preview(name = "DS200 - Hard", showBackground = true)
@Composable
fun Preview_DS200() {
    Box(
        modifier = Modifier.padding(10.dp) // 여백 추가
    ) {
        DS200(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.Hard
        )
    }
}

@Preview(name = "DS300 - Hard", showBackground = true)
@Composable
fun Preview_DS300() {
    Box(
        modifier = Modifier.padding(10.dp) // 여백 추가
    ) {
        DS300(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.Hard
        )
    }
}

@Preview(name = "DS400 - Hard", showBackground = true)
@Composable
fun Preview_DS400() {
    Box(
        modifier = Modifier.padding(10.dp) // 여백 추가
    ) {
        DS400(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.Hard
        )
    }
}
