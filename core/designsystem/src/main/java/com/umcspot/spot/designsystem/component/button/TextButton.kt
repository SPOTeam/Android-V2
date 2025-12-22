package com.umcspot.spot.designsystem.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.Dp.Companion.Unspecified
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
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
    B400State(
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
fun TextButtonSize.textStyle(): TextStyle = when (this) {
    TextButtonSize.XL, TextButtonSize.L -> SpotTheme.typography.h3
    TextButtonSize.M -> SpotTheme.typography.h4
    TextButtonSize.S, TextButtonSize.XS -> SpotTheme.typography.h5
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: TextButtonState = TextButtonState.B400State,
    size: TextButtonSize = TextButtonSize.M,
    width: Dp = Unspecified,
    shape: Shape = SpotShapes.Hard,
    enabled: Boolean = true,
    checked: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    content: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = state.resolveColors(enabled = enabled, isPressed = isPressed, checked = checked)

    val widthMod = if (width.isSpecified) Modifier.width(width) else Modifier.fillMaxWidth()

    Box(
        modifier = modifier
            .then(widthMod)
            .defaultMinSize(minHeight = size.minHeight)     // 최소 높이만 보장
            .semantics { role = Role.Button }
            .clip(shape)                                     // 클릭/리플 영역 클리핑
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // 배경/보더
        ShapeBox(
            shape = shape,
            color = colors.bg,
            borderWidth = 0.5.dp,
            borderColor = colors.border,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            when {
                content != null -> content()
                !text.isNullOrEmpty() -> Text(
                    text = text,
                    textAlign = TextAlign.Start,
                    style = size.textStyle(),
                    fontSize = size.fontSize,
                    color = colors.text,
                    maxLines = 1
                )
            }

            Spacer(Modifier.weight(1f))

        }
    }
}


// ---------- Size Convenience ----------

@Composable
fun TextButtonXL(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape : Shape = SpotShapes.Soft,
    width: Dp = 200.dp,
    enabled: Boolean = true,
    checked : Boolean = false,
    state: TextButtonState = TextButtonState.B400State,
    content: @Composable () -> Unit = {}
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    shape = shape,
    size = TextButtonSize.XL,
    width = width,
    enabled = enabled,
    checked = checked,
    state = state,
    content = content
)

@Composable
fun TextButtonL(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape : Shape = SpotShapes.Soft,
    width: Dp = 180.dp,
    enabled: Boolean = true,
    checked : Boolean = false,
    state: TextButtonState = TextButtonState.B400State,
    content: @Composable () -> Unit = {}
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    shape = shape,
    size = TextButtonSize.L,
    width = width,
    enabled = enabled,
    checked = checked,
    state = state,
    content = content
)

@Composable
fun TextButtonM(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 160.dp,
    shape : Shape = SpotShapes.Soft,
    enabled: Boolean = true,
    checked : Boolean = false,
    state: TextButtonState = TextButtonState.B400State,
    content: @Composable () -> Unit = {}
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonSize.M,
    shape = shape,
    width = width,
    enabled = enabled,
    checked = checked,
    state = state,
    content = content
)

@Composable
fun TextButtonS(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 140.dp,
    shape : Shape = SpotShapes.Soft,
    enabled: Boolean = true,
    checked : Boolean = false,
    state: TextButtonState = TextButtonState.B400State,
    content: @Composable () -> Unit = {}
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonSize.S,
    shape = shape,
    width = width,
    enabled = enabled,
    checked = checked,
    state = state,
    content = content
)

@Composable
fun TextButtonXS(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape : Shape = SpotShapes.Soft,
    width: Dp = 70.dp,
    enabled: Boolean = true,
    checked : Boolean = false,
    state: TextButtonState = TextButtonState.B400State,
    content: @Composable () -> Unit = {}
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = TextButtonSize.XS,
    shape = shape,
    width = width,
    enabled = enabled,
    checked = checked,
    state = state,
    content = content
)

@Composable
fun TextToggleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape : Shape = SpotShapes.Soft,
    width: Dp = 126.dp,
    enabled: Boolean = true,
    checked : Boolean = false,
    size: TextButtonSize = TextButtonSize.M,   // ← 기본값을 M로
    state: TextButtonState = TextButtonState.Toggle,
    content: @Composable () -> Unit = {}
) = TextButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    size = size,
    shape = shape,
    width = width,
    enabled = enabled,
    checked = checked,
    state = state,
    content = content
)