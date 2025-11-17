package com.umcspot.spot.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Dp.Companion.Unspecified
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White

data class MultiButtonColors(
    val bg: Color,
    val icon: Color,
    val text : Color,
)

enum class MultiButtonState(
    val normal: MultiButtonColors,
    val disabled: MultiButtonColors,
    val pressed: MultiButtonColors,
    val selected: MultiButtonColors
) {
    XOUTLINEState(
        normal = MultiButtonColors(
            bg = White,
            icon = Black,
            text = Black
        ),
        disabled = MultiButtonColors(
            bg = White,
            icon = G400,
            text = G400
        ),
        pressed = MultiButtonColors(
            bg = B200,
            icon = B400,
            text = B400
        ),
        selected = MultiButtonColors(
            bg = B100,
            icon = B400,
            text = B400
        )
    )
}

fun MultiButtonState.resolveColors(
    enabled: Boolean,
    isPressed: Boolean,
    checked: Boolean
): MultiButtonColors = when {
    !enabled -> disabled
    checked  -> selected
    isPressed -> pressed
    else -> normal
}

/** 이미지 전용 버튼 크기 토큰 */
enum class MultiButtonSize(
    val size: Dp,
    val icon: Dp,
    val text: TextUnit
) {
    XL(56.dp, 28.dp, 15.sp),
    L (52.dp, 24.dp, 15.sp),
    M (48.dp, 22.dp, 15.sp),
    S (44.dp, 20.dp, 15.sp),
    XS(40.dp, 18.dp, 15.sp);
}
@Composable
fun MultiButtonSize.textStyle(): TextStyle = when (this) {
    MultiButtonSize.XL, MultiButtonSize.L -> SpotTheme.typography.h4
    MultiButtonSize.M -> SpotTheme.typography.h4
    MultiButtonSize.S, MultiButtonSize.XS -> SpotTheme.typography.h5
}

private val MultiButtonSize.horizontalPadding: Dp get() = when (this) {
    MultiButtonSize.XL, MultiButtonSize.L -> 16.dp
    MultiButtonSize.M -> 14.dp
    MultiButtonSize.S -> 12.dp
    MultiButtonSize.XS -> 10.dp
}
private val MultiButtonSize.gap: Dp get() = when (this) {
    MultiButtonSize.XL, MultiButtonSize.L -> 8.dp
    MultiButtonSize.M, MultiButtonSize.S -> 6.dp
    MultiButtonSize.XS -> 4.dp
}

@Composable
fun MultiButtonSize.shape(): Shape = SpotShapes.Hard

@Composable
fun MultiButton(
    text: String,
    modifier: Modifier = Modifier,
    size: MultiButtonSize = MultiButtonSize.M,
    enabled: Boolean = true,
    state: MultiButtonState = MultiButtonState.XOUTLINEState,
    // 토글 유지
    checked: Boolean = false,
    width : Dp = Unspecified,
    // ✅ 단일 콜백: 컴포넌트가 계산한 newChecked 전달
    onClick: (newChecked: Boolean) -> Unit,
    // 아이콘 지정: painter가 우선
    painter: Painter? = null,

    // 아이콘 틴트 사용 여부 (true면 state의 icon 색상 사용)
    tintIcon: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val widthMod = if (width.isSpecified) Modifier.width(width) else Modifier.fillMaxWidth()

    val colors = state.resolveColors(
        enabled = enabled,
        isPressed = isPressed,
        checked = checked
    )

    // 접근성: 토글 가능 시 selected 노출
    val clickableModifier = modifier
        .then(widthMod)
        .heightIn(min = size.size)
        .semantics {
            role = Role.Button
            selected = checked
        }
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null
        ) {
            val newChecked = checked
            onClick(newChecked)
        }

    // 컨테이너: 보더 없이 배경만
    ShapeBox(
        shape = SpotShapes.Hard,
        color = colors.bg,
        borderWidth = 0.dp,
        borderColor = null,
        modifier = clickableModifier.height(size.size)   // ✅ 동일 로직 적용

    ) {
        Row(
            modifier = Modifier
                .height(size.size)
                .fillMaxWidth()
                .padding(horizontal = size.horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            painter?.let {
                if (tintIcon) {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        tint = colors.icon,
                        modifier = Modifier.size(size.icon)
                    )
                } else {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.size(size.icon)
                    )
                }
                Spacer(Modifier.width(size.gap))
            }

            // 텍스트
            Text(
                text = text,
                style = size.textStyle(),
                fontSize = size.text,
                color = colors.text,
                maxLines = 1
            )
        }
    }
}

@Composable
fun MultiButtonM(
    text: String,
    checked: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    width : Dp = Unspecified,
    state: MultiButtonState = MultiButtonState.XOUTLINEState,
    painter: Painter? = null,
    tintIcon: Boolean = false
) = MultiButton(
    text = text,
    modifier = modifier,
    size = MultiButtonSize.M,
    enabled = enabled,
    state = state,
    width = width,
    checked = checked,
    onClick = onClick,
    painter = painter,
    tintIcon = tintIcon
)

@Preview(showBackground = true)
@Composable
fun MultiButtonPreview() {
    SpotTheme {
        MultiButtonM(
            text = "온라인",
            onClick = {},
            modifier = Modifier.padding(10.dp),
            checked = false,
            width = 156.dp,
            painter = painterResource(R.drawable.language),
            tintIcon = false
        )
    }
}