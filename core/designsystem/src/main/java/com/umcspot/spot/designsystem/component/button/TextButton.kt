package com.umcspot.spot.designsystem.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.G500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White


// ---------- Color Tokens ----------

data class TextButtonColors(
    val bg: Color,
    val text: Color,
    val border: Color
)

enum class TextButtonState(
    val normal: TextButtonColors,
    val disabled: TextButtonColors,
    val pressed: TextButtonColors,
    val selected: TextButtonColors
) {
    B500State(
        normal = TextButtonColors(
            bg = White,
            text = B500,
            border = B500
        ),
        disabled = TextButtonColors(
            bg = White,
            text = G400,
            border = G300
        ),
        pressed = TextButtonColors(
            bg = B500,
            text = White,
            border = B500
        ),
        selected = TextButtonColors(
            bg = B500,
            text = White,
            border = B500
        )
    ),

    G500State(
        normal = TextButtonColors(
            bg = White,
            text = G500,
            border = G500
        ),
        disabled = TextButtonColors(
            bg = White,
            text = G400,
            border = G300
        ),
        pressed = TextButtonColors(
            bg = G500,
            text = White,
            border = G500
        ),
        selected = TextButtonColors(
            bg = G500,
            text = White,
            border = G500
        )
    ),


    // 거절 스타일
    R500State(
        normal = TextButtonColors(
            bg = White,
            text = R500,
            border = R500
        ),
        disabled = TextButtonColors(
            bg = White,
            text = G400,
            border = G300
        ),
        pressed = TextButtonColors(
            bg = R500,
            text = White,
            border = R500
        ),
        selected = TextButtonColors(
            bg = R500,
            text = White,
            border = R500
        )
    ),

    Toggle(
        normal = TextButtonColors(
            bg = White,
            text = Black,
            border = G200
        ),
        disabled = TextButtonColors(
            bg = White,
            text = G400,
            border = G200
        ),
        pressed = TextButtonColors(
            bg = B200,
            text = B500,
            border = B200
        ),
        selected = TextButtonColors(
            bg = B100,
            text = B500,
            border = B100
        )
    ),

    Click(
        normal = TextButtonColors(
        bg = White,
        text = Black,
        border = White
        ),
        disabled = TextButtonColors(
        bg = White,
        text = G400,
        border = White
        ),
        pressed = TextButtonColors(
        bg = B100,
        text = Black,
        border = B100
        ),
        selected = TextButtonColors(
        bg = B100,
        text = B500,
        border = B100
        )
    )
}

// 상태 팔레트 선택
fun TextButtonState.resolveColors(
    enabled: Boolean,
    isPressed: Boolean,
    checked: Boolean
): TextButtonColors = when {
    !enabled -> disabled
    checked  -> selected
    isPressed -> pressed
    else -> normal
}

// ---------- Size Tokens ----------

enum class TextButtonSize(
    val minHeight: Dp,
    val fontSize: TextUnit,
) {
    XL(56.dp, 15.sp),
    L (52.dp, 15.sp),
    M (48.dp, 15.sp),
    S (44.dp, 15.sp),
    XS(40.dp, 15.sp);
}

@Composable
fun ClickSurface(
    checked: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SpotShapes.Soft,
    state: TextButtonState = TextButtonState.Click,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = state.resolveColors(enabled = enabled, isPressed = isPressed, checked = checked)

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // 배경/보더만 상태 색으로
        ShapeBox(
            shape = shape,
            color = colors.bg,
            borderWidth = 0.5.dp,
            borderColor = colors.border,
            modifier = Modifier.matchParentSize()
        )

        // 패딩 + 텍스트 컬러 적용
        Box(Modifier.padding(contentPadding)) {
            CompositionLocalProvider(LocalContentColor provides colors.text) {
                content()
            }
        }
    }
}


@Composable
fun TextButton(
    text: String,
    style : TextStyle = SpotTheme.typography.h5,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: TextButtonState = TextButtonState.B500State,
    shape: Shape = SpotShapes.Hard,
    enabled: Boolean = true,
    checked: Boolean = false,
    content: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = state.resolveColors(enabled = enabled, isPressed = isPressed, checked = checked)

    Box(
        modifier = modifier
            .semantics { role = Role.Button }
            .clip(shape)                                     // 클릭/리플 영역 클리핑
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // 배경/보더
        ShapeBox(
            shape = shape,
            color = colors.bg,
            borderWidth = 1.dp,
            borderColor = colors.border,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .matchParentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = style,
                color = colors.text,
                maxLines = 1,
            )
        }
    }
}