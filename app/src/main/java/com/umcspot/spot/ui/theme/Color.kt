package com.umcspot.spot.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
/****************** base color ******************/
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


/****************** SPOT COLOR ******************/

/** Primary **/

val B500 = Color(0xFF005BFF)
val B400 = Color(0xFF337BFF)

/** Secondary **/

val R500 = Color(0xFFF34343)
val B200 = Color(0xFFD3E1FD)
val B100 = Color(0xFFEDF4FF)

/** Gray Scale **/

val Black = Color(0xFF1E1E1E)
val G500 = Color(0xFF4F4F56)
val G400 = Color(0xFF8F8F99)
val G300 = Color(0xFFC5C5CD)
val G200 = Color(0xFFF0F0F4)
val G100 = Color(0xFFF3F3F6)
val White = Color(0xFFFCFCFF)

/** Gradiant **/

val BlueGradient = Brush.linearGradient(
    colors = listOf(B400, B500),
    start = Offset(0f, 0f),    // A 지점
    end = Offset(1000f, 500f)  // B 지점 (대각선)
)

val GrayGradient = Brush.linearGradient(
    colors = listOf(G300, G500),
    start = Offset(0f, 0f),    // A 지점
    end = Offset(1000f, 500f)  // B 지점
)

/*
    Gradiant 적용 예시

    Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .background(BlueGradient) // 또는 GrayGradient
    )

 */