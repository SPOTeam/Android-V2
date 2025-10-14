package com.umcspot.spot.designsystem.shapes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.*
import com.umcspot.spot.designsystem.R

object SpotShapes {

    // ===== Shape 정의 (모서리) =====
    val Hard: Shape  = RoundedCornerShape(9.dp) // 20pt
    val Soft: Shape  = RoundedCornerShape(14.dp) // 32pt
    val Round: Shape = RoundedCornerShape(18.dp) // 42pt

    val SoftLeft: Shape  = RoundedCornerShape(
        topStart = 14.dp, topEnd = 0.dp, bottomStart = 14.dp, bottomEnd = 0.dp
    )
    val SoftRight: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 14.dp, bottomStart = 0.dp, bottomEnd = 14.dp
    )
    val RoundLeft: Shape  = RoundedCornerShape(
        topStart = 18.dp, topEnd = 0.dp, bottomStart = 18.dp, bottomEnd = 0.dp,
    )
    val RoundRight: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 18.dp, bottomStart = 0.dp, bottomEnd = 18.dp
    )
}

@Composable
fun ShapeImageWithBadge(
    painter: Painter = painterResource(R.drawable.spot_logo),
    contentDescription: String? = null,
    shape: Shape,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 0.dp,
    padding: Dp = 2.dp,
    borderColor: Color? = Color.Transparent,
    contentScale: ContentScale = ContentScale.Crop,
    badgeSize: Dp = 16.dp,
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        ShapeImageBox(
            painter = painter,
            contentDescription = contentDescription,
            shape = shape,
            size = size,
            modifier = modifier,
            borderWidth = borderWidth,
            padding = padding,
            borderColor = borderColor,
            contentScale = contentScale

        )

        Icon(
            painter = painterResource(R.drawable.announce),
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(badgeSize),
            contentDescription = null
        )
    }
}


@Composable
fun ShapeImageBox(
    painter: Painter,
    contentDescription: String? = null,
    shape: Shape,
    size: Dp = 20.dp,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 0.dp,
    padding: Dp = 5.dp,
    borderColor: Color? = Color.Transparent,
    contentScale: ContentScale = ContentScale.Crop,
    backgroundColor : Color = Color.Transparent,
    content: @Composable BoxScope.() -> Unit = {}   // ✅ 추가
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (borderColor != null && borderWidth > 0.dp) {
                    Modifier.border(borderWidth, borderColor, shape)
                } else {
                    Modifier
                }
            )
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.padding(padding)
        )

        content()
    }
}

@Composable
fun ShapeBox(
    shape: Shape,
    color: Color = SpotTheme.colors.white,
    borderWidth: Dp = 0.dp,
    borderColor: Color? = Color.Transparent,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}

) {
    Box(
        modifier = modifier
            .background(color, shape)
            .then(
                if (borderColor != null && borderWidth > 0.dp) {
                    Modifier.border(borderWidth, borderColor, shape)
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}

@Composable
fun StateCardActive(
    shape: Shape = SpotShapes.Hard,
    color: Color = SpotTheme.colors.white,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier
) = ShapeBox(

    shape = shape,
    color = color,
    borderWidth = borderWidth,
    borderColor = SpotTheme.colors.gray500,
    modifier = modifier
)

@Composable
fun StateCardSuccess(
    shape: Shape = SpotShapes.Hard,
    color: Color = SpotTheme.colors.white,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier
) = ShapeBox(
    shape = shape,
    color = color,
    borderWidth = borderWidth,
    borderColor = SpotTheme.colors.B400,
    modifier = modifier
)

@Composable
fun StateCardError(
    shape: Shape = SpotShapes.Hard,
    color: Color = SpotTheme.colors.white,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier
) = ShapeBox(
    shape = shape,
    color = color,
    borderWidth = borderWidth,
    borderColor = SpotTheme.colors.R500,
    modifier = modifier
)

@Composable
fun StateCardWarning(
    shape: Shape = SpotShapes.Hard,
    color: Color = SpotTheme.colors.white,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier
) = ShapeBox(
    shape = shape,
    color = color,
    borderWidth = borderWidth,
    borderColor = SpotTheme.colors.Y400,
    modifier = modifier
)


