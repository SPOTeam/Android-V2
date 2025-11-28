package com.umcspot.spot.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val B500 = Color(0xFF005BFF)
val B400 = Color(0xFF337BFF)

val Y400 = Color(0xFFFD8653)

val R500 = Color(0xFFF34343)
val B200 = Color(0xFFD3E1FD)
val B100 = Color(0xFFEDF4FF)

val Black = Color(0xFF1E1E1E)
val G500 = Color(0xFF4F4F56)
val G400 = Color(0xFF8F8F99)
val G300 = Color(0xFFC5C5CD)
val G200 = Color(0xFFF0F0F4)
val G100 = Color(0xFFF3F3F6)
val White = Color(0xFFFCFCFF)

val NaverGreen = Color(0xFF03CF5D)
val KakaoYellow = Color(0xFFFFEC00)
val KakaoText = Color(0xFF3C1E1E)

val BlueGradient = Brush.linearGradient(
    colors = listOf(B400, B500),
    start = Offset(0f, 0f),
    end = Offset(1000f, 500f)
)

val GrayGradient = Brush.linearGradient(
    colors = listOf(G300, G500),
    start = Offset(0f, 0f),
    end = Offset(1000f, 500f)
)

val SpotColors.B500: Color get() = primary
val SpotColors.B400: Color get() = primaryStrong
val SpotColors.B200: Color get() = primarySoft
val SpotColors.B100: Color get() = primarySoftest

val SpotColors.Y400: Color get() = secondary
val SpotColors.R500: Color get() = error

val SpotColors.Black: Color get() = black
val SpotColors.White: Color get() = white
val SpotColors.G500: Color get() = gray500
val SpotColors.G400: Color get() = gray400
val SpotColors.G300: Color get() = gray300
val SpotColors.G200: Color get() = gray200
val SpotColors.G100: Color get() = gray100
val SpotColors.Default: Color get() = default

val SpotColors.NaverGreen: Color get() = naverGreen
val SpotColors.KakaoYellow: Color get() = kakaoYellow
val SpotColors.KakaoText: Color get() = kakaoText

val SpotColors.BlackA80: Color get() = blackA80
val SpotColors.BlackA50: Color get() = blackA50
val SpotColors.WhiteA50: Color get() = whiteA50
val SpotColors.WhiteA10: Color get() = whiteA10

fun SpotColors.blueGradient(
    start: Offset = Offset(0f, 0f),
    end: Offset = Offset(1000f, 500f)
): Brush = Brush.linearGradient(listOf(B400, B500), start, end)

fun SpotColors.grayGradient(
    start: Offset = Offset(0f, 0f),
    end: Offset = Offset(1000f, 500f)
): Brush = Brush.linearGradient(listOf(G300, G500), start, end)

@Stable
class SpotColors(
    primary: Color,
    primaryStrong: Color,
    primarySoft: Color,
    primarySoftest: Color,
    secondary: Color,
    error: Color,
    gray500: Color,
    gray400: Color,
    gray300: Color,
    gray200: Color,
    gray100: Color,
    default: Color,
    black: Color,
    white: Color,
    naverGreen: Color,
    kakaoYellow: Color,
    kakaoText: Color,
    blackAlpha80: Color,
    blackAlpha50: Color,
    whiteAlpha50: Color,
    whiteAlpha10: Color,
    isLight: Boolean
) {
    var primary by mutableStateOf(primary); private set
    var primaryStrong by mutableStateOf(primaryStrong); private set
    var primarySoft by mutableStateOf(primarySoft); private set
    var primarySoftest by mutableStateOf(primarySoftest); private set

    var secondary by mutableStateOf(secondary); private set
    var error by mutableStateOf(error); private set

    var gray500 by mutableStateOf(gray500); private set
    var gray400 by mutableStateOf(gray400); private set
    var gray300 by mutableStateOf(gray300); private set
    var gray200 by mutableStateOf(gray200); private set
    var gray100 by mutableStateOf(gray100); private set
    var default by mutableStateOf(default); private set

    var black by mutableStateOf(black); private set
    var white by mutableStateOf(white); private set

    var naverGreen by mutableStateOf(naverGreen); private set
    var kakaoYellow by mutableStateOf(kakaoYellow); private set
    var kakaoText by mutableStateOf(kakaoText); private set

    var blackA80 by mutableStateOf(blackAlpha80); private set
    var blackA50 by mutableStateOf(blackAlpha50); private set
    var whiteA50 by mutableStateOf(whiteAlpha50); private set
    var whiteA10 by mutableStateOf(whiteAlpha10); private set

    var isLight by mutableStateOf(isLight)

    fun copy() = SpotColors(
        primary, primaryStrong, primarySoft, primarySoftest,
        secondary, error,
        gray500, gray400, gray300, gray200, gray100, default,
        black, white,
        naverGreen, kakaoYellow, kakaoText,
        blackA80, blackA50, whiteA50, whiteA10,
        isLight
    )

    fun update(colors: SpotColors) {
        primary = colors.primary
        primaryStrong = colors.primaryStrong
        primarySoft = colors.primarySoft
        primarySoftest = colors.primarySoftest

        secondary = colors.secondary
        error = colors.error

        gray500 = colors.gray500
        gray400 = colors.gray400
        gray300 = colors.gray300
        gray200 = colors.gray200
        gray100 = colors.gray100
        default = colors.default

        black = colors.black
        white = colors.white

        naverGreen = colors.naverGreen
        kakaoYellow = colors.kakaoYellow
        kakaoText = colors.kakaoText

        blackA80 = colors.blackA80
        blackA50 = colors.blackA50
        whiteA50 = colors.whiteA50
        whiteA10 = colors.whiteA10

        isLight = colors.isLight
    }
}

fun SpotDayColors(): SpotColors = SpotColors(
    primary = B500,
    primaryStrong = B400,
    primarySoft = B200,
    primarySoftest = B100,

    secondary = Y400,
    error = R500,

    gray500 = G500,
    gray400 = G400,
    gray300 = G300,
    gray200 = G200,
    gray100 = G100,
    default = G300,

    black = Black,
    white = White,

    naverGreen = NaverGreen,
    kakaoYellow = KakaoYellow,
    kakaoText = KakaoText,

    blackAlpha80 = Black.copy(alpha = 0.8f),
    blackAlpha50 = Black.copy(alpha = 0.5f),
    whiteAlpha50 = White.copy(alpha = 0.5f),
    whiteAlpha10 = White.copy(alpha = 0.1f),

    isLight = true
)