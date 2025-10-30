package com.umcspot.spot.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White

data class ImageButtonColors(
    val bg: Color,
    val icon: Color
)

enum class ImageButtonState(
    val normal: ImageButtonColors,
    val disabled: ImageButtonColors,
    val pressed: ImageButtonColors,
    val selected: ImageButtonColors
) {
    XOUTLINEState(
        normal = ImageButtonColors(
            bg = White,
            icon = Black
        ),
        disabled = ImageButtonColors(
            bg = White,
            icon = B400
        ),
        pressed = ImageButtonColors(
            bg = B200,
            icon = B400
        ),
        selected = ImageButtonColors(
            bg = B100,
            icon = B400
        )
    )
}

fun ImageButtonState.resolveColors(
    enabled: Boolean,
    isPressed: Boolean,
    checked: Boolean
): ImageButtonColors = when {
    !enabled -> disabled
    checked  -> selected
    isPressed -> pressed
    else -> normal
}

/** 이미지 전용 버튼 크기 토큰 */
enum class ImageButtonSize(
    val side: Dp,     // 버튼 한 변(정사각형)
    val icon: Dp      // 아이콘/이미지 한 변
) {
    XL(56.dp, 28.dp),
    L (52.dp, 24.dp),
    M (48.dp, 22.dp),
    S (44.dp, 20.dp),
    XS(40.dp, 18.dp);
}

@Composable
fun BlankButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp,
    enabled: Boolean = true,
    checked: Boolean = false,
    state: ImageButtonState = ImageButtonState.XOUTLINEState,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape : Shape = SpotShapes.Hard,
    content: @Composable BoxScope.() -> Unit = {}

) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = state.resolveColors(enabled = enabled, isPressed = isPressed, checked = checked)

    Box(
        modifier = modifier
            .defaultMinSize(size, size)
            .semantics { role = Role.Button }
            .clip(shape) // 클릭 영역과 모서리 일치
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // ✅ 보더 없이 배경만: ShapeBox 재사용
        ShapeBox(
            shape = SpotShapes.Hard,
            color = colors.bg,       // 상태별 배경색
            borderWidth = 0.dp,      // 테두리 없음
            borderColor = null,      // 안전하게 보더 비활성화
            modifier = Modifier
                .matchParentSize() // 부모 Box와 동일 크기
        )

        content()
    }
}


@Preview
@Composable
fun preview() {
    SpotTheme {
        BlankButton(
            modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 13.dp, vertical = 8.dp),
            size = 71.dp,
            onClick = {}
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.prefer_location),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "내 지역",
                    style = SpotTheme.typography.bodyMedium600,
                    fontSize = 14.sp,
                    color = Black,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ImageButtonSize = ImageButtonSize.M,
    enabled: Boolean = true,
    checked: Boolean = false,
    state: ImageButtonState = ImageButtonState.XOUTLINEState,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },

    painter: Painter? = null,

    useTint: Boolean = true,
    contentDescription: String? = null,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = state.resolveColors(enabled = enabled, isPressed = isPressed, checked = checked)

    Box(
        modifier = modifier
            .size(size.side)
            .semantics { role = Role.Button }
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // ✅ 보더 없이 배경만: ShapeBox 재사용
        ShapeBox(
            shape = SpotShapes.Hard,
            color = colors.bg,       // 상태별 배경색
            borderWidth = 0.dp,      // 테두리 없음
            borderColor = null,      // 안전하게 보더 비활성화
            modifier = Modifier.size(size.side)
        )

        painter?.let { p ->
            if (useTint) {
                Icon(
                    painter = p,
                    contentDescription = contentDescription,
                    tint = colors.icon,
                    modifier = Modifier.size(size.icon)
                )
            } else {
                Image(
                    painter = p,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(size.icon)
                )
            }
        }
    }
}


// --- 편의 함수: Painter 버전
@Composable
fun ImageButtonPainterM(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: ImageButtonState = ImageButtonState.XOUTLINEState,
    useTint: Boolean = true,
    checked: Boolean = false,
    contentDescription: String? = null
) = ImageButton(
    onClick = onClick,
    modifier = modifier,
    size = ImageButtonSize.M,
    enabled = enabled,
    state = state,
    painter = painter,
    useTint = useTint,
    checked = checked,
    contentDescription = contentDescription
)

@Preview(showBackground = true)
@Composable
fun ImageButtonMPreview() {
    SpotTheme {
        ImageButtonPainterM(
            modifier = Modifier.padding(10.dp),
            painter = painterResource(R.drawable.search),
            onClick = {},
            checked = false,
            useTint = true,
            contentDescription = "검색"
        )
    }
}