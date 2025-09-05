package com.umcspot.spot.ui.component.button.r500

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape // ✅ compose Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umcspot.spot.ui.shapes.ShapeBox
import com.umcspot.spot.ui.shapes.SpotShapes
import com.umcspot.spot.ui.theme.R500
import com.umcspot.spot.ui.theme.G300
import com.umcspot.spot.ui.theme.G400
import com.umcspot.spot.ui.theme.SpotTypography
import com.umcspot.spot.ui.theme.White
import androidx.compose.material3.Text
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.umcspot.spot.ui.theme.Y400

// ---------------------- //
//        Tokens          //
// ---------------------- //

enum class TextButtonR500Size(
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
fun TextButtonR500(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: TextButtonR500Size = TextButtonR500Size.M,
    checked: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    // 상태별 토큰 매핑
    val bgColor = when {
        !enabled -> White
        checked -> Y400
        isPressed -> R500
        else -> White
    }
    val textColor = when {
        !enabled -> G400
        checked -> White
        isPressed -> White
        else -> R500
    }
    val borderColor = when {
        !enabled -> G300
        checked -> Y400
        isPressed -> R500
        else -> R500
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
fun TextButtonR500XL(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonR500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonR500Size.XL,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonR500L(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonR500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonR500Size.L,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonR500M(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonR500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonR500Size.M,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonR500S(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonR500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonR500Size.S,
    checked = checked,
    enabled = enabled
)

@Composable
fun TextButtonR500XS(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
) = TextButtonR500(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonR500Size.XS,
    checked = checked,
    enabled = enabled
)