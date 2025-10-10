package com.example.core.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.core.ui.theme.B400
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.Black
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.G400
import com.example.core.ui.theme.G500
import com.example.core.ui.theme.R500
import com.example.core.ui.theme.SpotTypography
import com.example.core.ui.theme.White

// ---------- Color Tokens ----------

data class ButtonColors(
    val bg: Color,
    val text: Color,
    val border: Color
)

enum class ButtonState(
    val normal: ButtonColors,
    val disabled: ButtonColors,
    val pressed: ButtonColors,
    val checked: ButtonColors
) {
    B400State(
        normal = ButtonColors(
            bg = White,
            text = B500,
            border = B500
        ),
        disabled = ButtonColors(
            bg = White,
            text = G400,
            border = G300
        ),
        pressed = ButtonColors(
            bg = B500,
            text = White,
            border = B500
        ),
        checked = ButtonColors(
            bg = B500,
            text = White,
            border = B500
        )
    ),

    G500State(
        normal = ButtonColors(
            bg = White,
            text = G500,
            border = G500
        ),
        disabled = ButtonColors(
            bg = White,
            text = G400,
            border = G300
        ),
        pressed = ButtonColors(
            bg = G500,
            text = White,
            border = G500
        ),
        checked = ButtonColors(
            bg = G500,
            text = White,
            border = G500
        )
    ),


    // 거절 스타일
    R500State(
        normal = ButtonColors(
            bg = White,
            text = R500,
            border = R500
        ),
        disabled = ButtonColors(
            bg = White,
            text = G400,
            border = G300
        ),
        pressed = ButtonColors(
            bg = R500,
            text = White,
            border = R500
        ),
        checked = ButtonColors(
            bg = R500,
            text = White,
            border = R500
        )
    ),
}

// 상태 팔레트 선택
private fun ButtonState.resolveColors(enabled: Boolean, isPressed: Boolean): ButtonColors =
    when {
        !enabled -> disabled
        isPressed -> pressed
        else -> normal
    }

// ---------- Size Tokens ----------

enum class TextButtonSize(
    val minHeight: Dp,
    val shape: Shape,
    val textStyle: TextStyle,
    val fontSize: TextUnit
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

// ---------- Component ----------

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: TextButtonSize = TextButtonSize.M,
    width: Dp = 160.dp,
    enabled: Boolean = true,
    state: ButtonState = ButtonState.B400State,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = state.resolveColors(enabled = enabled, isPressed = isPressed)

    Box(
        modifier = modifier
            .width(width)
            .heightIn(min = size.minHeight)
            .semantics { role = Role.Button }
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null, // 필요 시 리플로 교체
                onClick = onClick
            )
    ) {
        // 배경/보더
        ShapeBox(
            shape = size.shape,
            color = colors.bg,
            borderWidth = 0.5.dp,
            borderColor = colors.border,
            modifier = Modifier
                .width(width)
                .height(size.minHeight)
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
                color = colors.text,
                maxLines = 1
            )
        }
    }
}

// ---------- Size Convenience ----------

@Composable
fun TextButtonXL(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 200.dp,
    enabled: Boolean = true,
    state: ButtonState = ButtonState.B400State,
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonSize.XL,
    width = width,
    enabled = enabled,
    state = state
)

@Composable
fun TextButtonL(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 180.dp,
    enabled: Boolean = true,
    state: ButtonState = ButtonState.B400State,
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonSize.L,
    width = width,
    enabled = enabled,
    state = state
)

@Composable
fun TextButtonM(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 160.dp,
    enabled: Boolean = true,
    state: ButtonState = ButtonState.B400State,
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonSize.M,
    width = width,
    enabled = enabled,
    state = state
)

@Composable
fun TextButtonS(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 140.dp,
    enabled: Boolean = true,
    state: ButtonState = ButtonState.B400State,
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonSize.S,
    width = width,
    enabled = enabled,
    state = state
)

@Composable
fun TextButtonXS(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 70.dp,
    enabled: Boolean = true,
    state: ButtonState = ButtonState.B400State,
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonSize.XS,
    width = width,
    enabled = enabled,
    state = state
)
