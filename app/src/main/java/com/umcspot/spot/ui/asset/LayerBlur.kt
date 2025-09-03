package com.umcspot.spot.ui.asset

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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

// 공통: Layer blur 적용(요소 자체 흐림)
private fun Modifier.layerBlur(blur: Dp, cornerRadius: Dp): Modifier {
    val shaped = this.clip(RoundedCornerShape(cornerRadius))
    return if (Build.VERSION.SDK_INT >= 31) {
        shaped.blur(blur)
        // 또는 RenderEffect 사용
        //.graphicsLayer {
        //    val r = blur.toPx()
        //    renderEffect = RenderEffect.createBlurEffect(r, r, Shader.TileMode.CLAMP)
        //}
    } else {
        shaped // API 30 이하는 의미 있는 레이어 블러 미지원 → 모서리만 클립
    }
}

// ===== L1~L4 프리셋 (xdp, ydp, image만 받음) =====

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

// 내부 공통 구현
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

@Preview(name = "Layer Blur Presets", showBackground = true, widthDp = 500)
@Composable
fun PreviewLayerBlurs() {
    val sample = painterResource(id = R.drawable.spot_logo) // res/drawable/sample.jpg

    Row(
        modifier = Modifier.padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        L1(100.dp, 180.dp, image = sample)
        L2(100.dp, 180.dp, image = sample)
        L3(100.dp, 180.dp, image = sample)
        L4(100.dp, 180.dp, image = sample)
    }
}