package com.umcspot.spot.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.G500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun SpotActivationButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
    style: TextStyle = SpotTheme.typography.h5
) {
    val borderColor = if (isEnabled) SpotTheme.colors.B500 else SpotTheme.colors.G300
    val textColor = if (isEnabled) SpotTheme.colors.B500 else SpotTheme.colors.G400
    val backgroundColor = if (isEnabled) Color.Transparent else Color.Transparent
    val shape = RoundedCornerShape(10.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape) 
            .border( 
                width = 1.dp, 
                color = borderColor,
                shape = shape
            )
            .background(color = backgroundColor) 
            .noRippleClickable(onClick = { if (isEnabled) onClick() })
            .padding(vertical = screenHeightDp(9.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buttonText,
            style = style,
            color = textColor 
        )
    }
}

@Composable
fun SpotCancelButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = SpotTheme.colors.G500
    val textColor = SpotTheme.colors.G500
    val shape = RoundedCornerShape(10.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .noRippleClickable(onClick = onClick)
            .padding(vertical = screenHeightDp(9.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buttonText,
            style = SpotTheme.typography.h5,
            color = textColor
        )
    }
}
