package com.example.core.ui.component.button.b100

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.shapes.ShapeBox
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.B100
import com.example.core.ui.theme.B200
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.Black
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.G400
import com.example.core.ui.theme.SpotTypography
import com.example.core.ui.theme.White

// ---------------------- //
//        Tokens          //
// ---------------------- //

enum class TextButtonB100Size(
    val minHeight: Dp,
    val shape: Shape,
    val textStyle: TextStyle,
    val fontSize : TextUnit
) {
    XL(
        minHeight = 56.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header03,
        fontSize = 15.sp
    ),
    L(
        minHeight = 52.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header03,
        fontSize = 15.sp

    ),
    M(
        minHeight = 48.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header04,
        fontSize = 15.sp

    ),
    S(
        minHeight = 44.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header05,
        fontSize = 15.sp
    ),
    XS(
        minHeight = 40.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header05,
        fontSize = 15.sp
    )
}

@Composable
fun TextButtonB100(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: TextButtonB100Size = TextButtonB100Size.M,
    width: Dp = 160.dp,              // ✅ 에셋용 기본 폭 (원하면 바꿔 쓰기)
    checked: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    // 상태별 토큰 매핑
    val bgColor = when {
        !enabled -> White
        checked -> B100
        isPressed -> B200
        else -> White
    }
    val textColor = when {
        !enabled -> G400
        checked -> B500
        isPressed -> B500
        else -> Black
    }
    val borderColor = when {
        !enabled -> G300
        checked -> B500
        isPressed -> G300
        else -> G300
    }

    // 배경/보더를 먼저 그림 (ShapeBox), 그 위에 텍스트 오버레이
    Box(
        modifier = modifier
            .heightIn(min = size.minHeight)
            .semantics { role = Role.Button }
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null, // 필요시 기본 리플 끄기
                onClick = onClick
            )
    ) {
        // 배경 박스
        ShapeBox(
            width = width,
            height = size.minHeight,
            shape = size.shape,
            color = bgColor,
            borderWidth = 0.5.dp,
            borderColor = borderColor
        )

        // 콘텐츠 (정중앙)
        Box(
            modifier = Modifier.size(width, size.minHeight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = size.textStyle,
                fontSize = size.fontSize,
                color = textColor,
                maxLines = 1
            )
        }
    }
}

// ---------------------- //
//   Size Convenience     //
// ---------------------- //

@Composable
fun TextButtonB100XL(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 200.dp,             // XL 권장 폭 예시
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonB100(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonB100Size.XL,
    width = width,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonB100L(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 180.dp,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonB100(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonB100Size.L,
    width = width,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonB100M(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 160.dp,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonB100(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonB100Size.M,
    width = width,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonB100S(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 140.dp,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonB100(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonB100Size.S,
    width = width,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonB100XS(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 70.dp,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonB100(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonB100Size.XS,
    width = width,
    checked = checked,
    enabled = enabled
)