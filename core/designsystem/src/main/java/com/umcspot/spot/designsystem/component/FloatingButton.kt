package com.umcspot.spot.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.*
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.ui.extension.noRippleClickable

@Composable
fun FloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    backgroundColor: Color = SpotTheme.colors.B400,
    contentColor: Color = SpotTheme.colors.White,
    elevation: Dp = 3.dp,
    iconSize: Dp = 23.dp,
    @DrawableRes iconRes: Int = R.drawable.multiple
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(elevation, CircleShape, clip = false)
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "Add",
            tint = contentColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun FloatingToUpButton (
    modifier : Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingButton(
        modifier = modifier,
        onClick = onClick,
        backgroundColor = White,
        contentColor = B500,
        iconRes = R.drawable.arrow_to_the_top
    )
}

@Composable
fun FloatingMultipleButton (
    modifier : Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingButton(
        modifier = modifier,
        onClick = onClick,
        iconRes = R.drawable.multiple
    )
}

/* =================== Previews =================== */

@Preview(showBackground = true)
@Composable
fun Preview_SpotFabSolid_TOTOP() {
    SpotTheme{
        FloatingToUpButton(
            onClick = {},
            modifier = Modifier.padding(6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_SpotFabSolid_MULTIPLE() {
    SpotTheme{
        FloatingMultipleButton(
            onClick = {},
            modifier = Modifier.padding(6.dp)
        )
    }
}

