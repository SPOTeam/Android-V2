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
import com.umcspot.spot.ui.extension.screenWidthDp

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
    ),

    XOUTLINETransparentState(
        normal = ImageButtonColors(
            bg = Color.Transparent,
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
    checked -> selected
    isPressed -> pressed
    else -> normal
}

@Composable
fun BlankButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean = false,
    state: ImageButtonState = ImageButtonState.XOUTLINEState,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = SpotShapes.Hard,
    content: @Composable BoxScope.() -> Unit = {}

) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = state.resolveColors(enabled = enabled, isPressed = isPressed, checked = checked)

    Box(
        modifier = modifier
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
private fun preview() {
    SpotTheme {
        BlankButton(
            modifier = Modifier
                .padding(screenWidthDp(13.dp))
                .size(screenWidthDp(71.dp)),
            onClick = {}
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.prefer_location),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(screenWidthDp(31.dp))
                )
                Spacer(Modifier.height(screenWidthDp(7.dp)))
                Text(
                    text = "내 지역",
                    style = SpotTheme.typography.regular_500,
                    color = Black,
                    maxLines = 1
                )
            }
        }
    }
}
