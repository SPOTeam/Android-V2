package com.example.core.ui.component.global

import android.content.Context
import android.util.TypedValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// Composable 아님, Context 필요
fun ptToSp(context: Context, pt: Float): TextUnit {
    val metrics = context.resources.displayMetrics
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, pt, metrics)
    return (px / metrics.scaledDensity).sp
}
