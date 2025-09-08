package com.example.core.ui.component.button.g500

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
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.G400
import com.example.core.ui.theme.G500
import com.example.core.ui.theme.SpotTypography
import com.example.core.ui.theme.White

// ---------------------- //
//        Tokens          //
// ---------------------- //

enum class TextButtonG500Size(
    val minWidth : Dp,
    val minHeight: Dp,
    val shape: Shape,
    val textStyle: TextStyle,
    val fontSize : TextUnit
) {
    XL(
        minWidth = 200.dp,
        minHeight = 56.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header03,
        fontSize = 15.sp
    ),
    L(
        minWidth = 180.dp,
        minHeight = 52.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header03,
        fontSize = 15.sp

    ),
    M(
        minWidth = 160.dp,
        minHeight = 48.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header04,
        fontSize = 15.sp

    ),
    S(
        minWidth = 140.dp,
        minHeight = 44.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header05,
        fontSize = 15.sp
    ),
    XS(
        minWidth = 70.dp,
        minHeight = 40.dp,
        shape = SpotShapes.Hard,
        textStyle = SpotTypography.header05,
        fontSize = 15.sp
    )
}

@Composable
fun TextButtonG500(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: TextButtonG500Size = TextButtonG500Size.M,
    checked: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    // 상태별 토큰 매핑
    val bgColor = when {
        !enabled -> White
        checked -> G400
        isPressed -> G500
        else -> White
    }
    val textColor = when {
        !enabled -> G400
        checked -> White
        isPressed -> White
        else -> G500
    }
    val borderColor = when {
        !enabled -> G300
        checked -> G400
        isPressed -> G500
        else -> G500
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
            width = size.minWidth,
            height = size.minHeight,
            shape = size.shape,
            color = bgColor,
            borderWidth = 1.dp,
            borderColor = borderColor
        )

        // 콘텐츠 (정중앙)
        Box(
            modifier = Modifier.size(size.minWidth, size.minHeight),
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
fun TextButtonG500XL(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonG500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonG500Size.XL,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonG500L(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonG500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonG500Size.L,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonG500M(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonG500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonG500Size.M,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonG500S(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonG500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonG500Size.S,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonG500XS(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonG500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonG500Size.XS,
    checked = checked,
    enabled = enabled
)