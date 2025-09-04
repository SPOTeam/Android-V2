package com.umcspot.spot.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umcspot.spot.R
import com.umcspot.spot.ui.theme.B500
import com.umcspot.spot.ui.theme.White

@Composable
fun FloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    backgroundColor: Color = B500,
    contentColor: Color = White,
//    elevation: Dp = 6.dp,
    iconSize: Dp = 40.dp,
    @DrawableRes iconRes: Int = R.drawable.multiple
) {
    Box(
        modifier = modifier
            .size(size)
//            .shadow(elevation, CircleShape, clip = false)
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
            .clickable(role = Role.Button, onClick = onClick),
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

/* =================== Previews =================== */

@Preview(showBackground = true)
@Composable
fun Preview_SpotFabSolid_Custom() {
    FloatingButton(
        onClick = {},
        modifier = Modifier.padding(6.dp)
    )
}
