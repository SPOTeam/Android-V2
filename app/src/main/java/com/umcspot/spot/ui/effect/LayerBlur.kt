package com.umcspot.spot.ui.effect

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umcspot.spot.R

private fun Modifier.layerBlur(blur: Dp, cornerRadius: Dp): Modifier {
    val shaped = this.clip(RoundedCornerShape(cornerRadius))
    return if (Build.VERSION.SDK_INT >= 31) {
        shaped.blur(blur)
    } else {
        shaped
    }
}

@Composable
fun L1(xdp: Dp, ydp: Dp, image: Painter, cornerRadius: Dp = 20.dp) {
    BaseLayerBlurImage(xdp, ydp, image, blur = 1.dp, cornerRadius = cornerRadius)
}

@Composable
fun L2(xdp: Dp, ydp: Dp, image: Painter, cornerRadius: Dp = 20.dp) {
    BaseLayerBlurImage(xdp, ydp, image, blur = 2.dp, cornerRadius = cornerRadius)
}

@Composable
fun L3(xdp: Dp, ydp: Dp, image: Painter, cornerRadius: Dp = 20.dp) {
    BaseLayerBlurImage(xdp, ydp, image, blur = 3.dp, cornerRadius = cornerRadius)
}

@Composable
fun L4(xdp: Dp, ydp: Dp, image: Painter, cornerRadius: Dp = 20.dp) {
    BaseLayerBlurImage(xdp, ydp, image, blur = 4.dp, cornerRadius = cornerRadius)
}

@Composable
private fun BaseLayerBlurImage(
    xdp: Dp,
    ydp: Dp,
    image: Painter,
    blur: Dp,
    cornerRadius: Dp
) {
    Box(
        modifier = Modifier
            .size(xdp, ydp)
            .layerBlur(blur = blur, cornerRadius = cornerRadius)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.matchParentSize().clip(RoundedCornerShape(cornerRadius)),
            contentScale = ContentScale.Crop
        )
    }
}

/* =================== Previews =================== */

@Preview(name = "L1 Blur", showBackground = true)
@Composable
fun PreviewL1() {
    val sample = painterResource(id = R.drawable.spot_logo)
    L1(120.dp, 180.dp, image = sample)
}

@Preview(name = "L2 Blur", showBackground = true)
@Composable
fun PreviewL2() {
    val sample = painterResource(id = R.drawable.spot_logo)
    L2(120.dp, 180.dp, image = sample)
}

@Preview(name = "L3 Blur", showBackground = true)
@Composable
fun PreviewL3() {
    val sample = painterResource(id = R.drawable.spot_logo)
    L3(120.dp, 180.dp, image = sample)
}

@Preview(name = "L4 Blur", showBackground = true)
@Composable
fun PreviewL4() {
    val sample = painterResource(id = R.drawable.spot_logo)
    L4(120.dp, 180.dp, image = sample)
}
