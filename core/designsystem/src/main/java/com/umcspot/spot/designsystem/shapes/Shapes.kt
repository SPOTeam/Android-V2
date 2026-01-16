package com.umcspot.spot.designsystem.shapes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.umcspot.spot.designsystem.theme.*
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.ui.extension.screenWidthDp

object SpotShapes {

    val HardDp : Dp = 6.dp
    val SoftDp : Dp = 10.dp
    val RoundDp : Dp = 14.dp


    // ===== Shape 정의 (모서리) =====
    val Hard: Shape  = RoundedCornerShape(HardDp)
    val Soft: Shape  = RoundedCornerShape(SoftDp)
    val Round: Shape = RoundedCornerShape(RoundDp)

    val SoftLeft: Shape  = RoundedCornerShape(
        topStart = SoftDp, topEnd = 0.dp, bottomStart = SoftDp, bottomEnd = 0.dp
    )
    val SoftRight: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = SoftDp, bottomStart = 0.dp, bottomEnd = SoftDp
    )

    val SoftTop: Shape  = RoundedCornerShape(
        topStart = SoftDp, topEnd = SoftDp, bottomStart = 0.dp, bottomEnd = 0.dp
    )
    val SoftBottom: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 0.dp, bottomStart = SoftDp, bottomEnd = SoftDp
    )

    val RoundLeft: Shape  = RoundedCornerShape(
        topStart = RoundDp, topEnd = 0.dp, bottomStart = RoundDp, bottomEnd = 0.dp,
    )
    val RoundRight: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = RoundDp, bottomStart = 0.dp, bottomEnd = RoundDp
    )

    val RoundTop: Shape  = RoundedCornerShape(
        topStart = RoundDp, topEnd = RoundDp, bottomStart = 0.dp, bottomEnd = 0.dp,
    )
    val RoundBottom: Shape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 0.dp, bottomStart = RoundDp, bottomEnd = RoundDp
    )
}

@Composable
fun ShapeImageWithBadge(
    imageRef: ImageRef = ImageRef.None,
    shape: Shape,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 0.dp,
    padding: Dp = 2.dp,
    borderColor: Color? = Color.Transparent,
    contentScale: ContentScale = ContentScale.Fit,
    badgeSize: Dp = 16.dp,
) {
    Box(
        modifier = modifier.size(screenWidthDp(size)),
        contentAlignment = Alignment.Center
    ) {
        ShapeImageBox(
            modifier = Modifier.size(screenWidthDp(size)),
            imageRef = imageRef,
            shape = shape,
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
                .size(screenWidthDp(badgeSize)),
            contentDescription = null
        )
    }
}


@Composable
fun ShapeImageBox(
    imageRef: ImageRef,
    modifier: Modifier = Modifier,
    shape: Shape,
    borderWidth: Dp = 0.dp,
    padding: Dp = 5.dp,
    borderColor: Color? = Color.Transparent,
    contentScale: ContentScale = ContentScale.Fit,
    backgroundColor: Color = Color.Transparent,
    fallback: Int = R.drawable.spot_logo,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val context = LocalContext.current

    val painter: Painter? = when (imageRef) {
        ImageRef.None -> painterResource(fallback)

        is ImageRef.Name -> {
            val resId = remember(imageRef.name) {
                context.resources.getIdentifier(
                    imageRef.name,
                    "drawable",
                    context.packageName
                )
            }
            if (resId != 0) painterResource(resId) else painterResource(fallback)
        }

        is ImageRef.Url ->
            rememberAsyncImagePainter(model = imageRef.url)

        is ImageRef.LocalUri ->
            rememberAsyncImagePainter(model = imageRef.uri)
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (borderColor != null && borderWidth > 0.dp) {
                    Modifier.border(borderWidth, borderColor, shape)
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(padding)
            )
        }
        content()
    }
}


@Composable
fun ShapeBox(
    shape: Shape = SpotShapes.Hard,
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


