package com.umcspot.spot.feature.board.post.posting

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImagePainter
import kotlin.math.max
import kotlin.math.min

@Composable
fun rememberDismissTintFromPainter(
    painterState: AsyncImagePainter.State,
    // dismiss가 TopEnd에 있으니, 그 근처만 샘플링
    sampleCorner: Corner = Corner.TopEnd,
    threshold: Float = 0.55f, // 0~1, 높을수록 "밝다" 판정이 쉬움
): Color {
    var tint by remember { mutableStateOf(Color.Black) }

    LaunchedEffect(painterState) {
        val success = painterState as? AsyncImagePainter.State.Success ?: return@LaunchedEffect
        val drawable = success.result.drawable

        val bitmap = (drawable as? BitmapDrawable)?.bitmap ?: return@LaunchedEffect
        val luma = sampleCornerLuma(bitmap, sampleCorner)

        // 어두우면 흰색 아이콘, 밝으면 검은색 아이콘
        tint = if (luma < threshold) Color.White else Color.Black
    }

    return tint
}

enum class Corner { TopStart, TopEnd, BottomStart, BottomEnd }

private fun sampleCornerLuma(bitmap: Bitmap, corner: Corner): Float {
    // HARDWARE bitmap이면 픽셀 접근 불가 -> ARGB_8888로 복사
    val safeBitmap = if (bitmap.config == Bitmap.Config.HARDWARE) {
        bitmap.copy(Bitmap.Config.ARGB_8888, /* mutable = */ false)
    } else {
        bitmap
    }

    val w = safeBitmap.width
    val h = safeBitmap.height
    val sw = max(1, (w * 0.20f).toInt())
    val sh = max(1, (h * 0.20f).toInt())

    val (startX, startY) = when (corner) {
        Corner.TopStart -> 0 to 0
        Corner.TopEnd -> (w - sw) to 0
        Corner.BottomStart -> 0 to (h - sh)
        Corner.BottomEnd -> (w - sw) to (h - sh)
    }

    var sum = 0f
    var count = 0

    val stepX = max(1, sw / 30)
    val stepY = max(1, sh / 30)

    for (y in startY until (startY + sh) step stepY) {
        for (x in startX until (startX + sw) step stepX) {
            val c = safeBitmap.getPixel(min(x, w - 1), min(y, h - 1))
            val r = ((c shr 16) and 0xFF) / 255f
            val g = ((c shr 8) and 0xFF) / 255f
            val b = (c and 0xFF) / 255f
            val luma = 0.2126f * r + 0.7152f * g + 0.0722f * b
            sum += luma
            count++
        }
    }

    // copy로 만든 비트맵이면 해제(메모리)
    if (safeBitmap !== bitmap) safeBitmap.recycle()

    return if (count == 0) 1f else sum / count
}

