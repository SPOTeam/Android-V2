package com.umcspot.spot.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

data class MultiButtonColors(
    val bg: Color,
    val icon: Color,
    val text: Color,
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
            text = B500,
        ),
        selected = MultiButtonColors(
            bg = B100,
            icon = B400,
            text = B500
        )
    )
}

fun MultiButtonState.resolveColors(
    enabled: Boolean,
    isPressed: Boolean,
    checked: Boolean
): MultiButtonColors = when {
    !enabled -> disabled
    checked -> selected
    isPressed -> pressed
    else -> normal
}

@Composable
fun MultiButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape : Shape = SpotShapes.Hard,
    state: MultiButtonState = MultiButtonState.XOUTLINEState,
    checked: Boolean = false,
    onClick: (newChecked: Boolean) -> Unit,
    painter: Painter? = null,
    tintIcon: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val internalSource = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by internalSource.collectIsPressedAsState()

    val colors = state.resolveColors(
        enabled = enabled,
        isPressed = isPressed,
        checked = checked
    )

    Box(
        modifier = modifier
            .semantics {
                role = Role.Button
                selected = checked
            }
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick(!checked)
            }
    ) {
        ShapeBox(
            shape = shape,
            color = colors.bg,
            borderWidth = 0.dp,
            borderColor = null,
            modifier = Modifier
                .width(screenWidthDp(156.dp))
                .height(screenHeightDp(43.dp))
        ) {
            Box(
                modifier = Modifier.matchParentSize(), 
                contentAlignment = Alignment.CenterStart 
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = screenWidthDp(8.dp))
                ) {
                    painter?.let {
                        val iconModifier = Modifier.size(screenHeightDp(33.dp))
                        if (tintIcon) {
                            Icon(
                                painter = it,
                                contentDescription = null,
                                tint = colors.icon,
                                modifier = iconModifier
                            )
                        } else {
                            Image(
                                painter = it,
                                contentDescription = null,
                                modifier = iconModifier
                            )
                        }
                        Spacer(Modifier.width(screenWidthDp(8.dp)))
                    }
                    Text(
                        text = text,
                        style = SpotTheme.typography.h4,
                        color = colors.text,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultiButtonPreview() {
    SpotTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MultiButton(
                    text = "어학",
                    onClick = {},
                    checked = true,
                    painter = painterResource(R.drawable.language),
                )
                MultiButton(
                    text = "자격증",
                    onClick = {},
                    checked = false,
                    painter = painterResource(R.drawable.license),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MultiButton(
                    text = "프로젝트",
                    onClick = {},
                    checked = false,
                    painter = painterResource(R.drawable.project),
                )
            }
        }
    }
}
