package com.umcspot.spot.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.umcspot.spot.R

val Suit = FontFamily(
    Font(R.font.suit_variable)
)
data class CustomTypography (
    val header01: TextStyle,
    val header02: TextStyle,
    val header03: TextStyle,
    val header04: TextStyle,
    val header05: TextStyle,
    val bodyLarge600: TextStyle,
    val bodyLarge500: TextStyle,
    val bodyMedium600: TextStyle,
    val bodyMedium500: TextStyle,
    val bodyRegular500: TextStyle,
    val bodyRegular400: TextStyle,
    val bodySmall500: TextStyle,
    val bodySmall400: TextStyle,
    val bodySmall300: TextStyle,
)

val SpotTypography = CustomTypography (
    header01 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp, // 64pt ≈ 85sp
        lineHeight = (85.sp * 1.5f), // 150%
    ),
    header02 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp, // 52pt ≈ 69sp
        lineHeight = (69.sp * 1.54f), // 154%
    ),
    header03 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp, // 48pt ≈ 64sp
        lineHeight = (64.sp * 1.5f), // 150%
    ),
    header04 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp, // 44pt ≈ 58sp
        lineHeight = (58.sp * 1.54f), // 154%
    ),
    header05 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp, // 40pt ≈ 53sp
        lineHeight = (53.sp * 1.5f), // 150%
    ),
    bodyLarge600 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 44.sp,
        lineHeight = (58.sp * 1.46f) // 146%
    ),
    bodyLarge500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 44.sp,
        lineHeight = (58.sp * 1.46f) // 146%
    ),
    bodyMedium600 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        lineHeight = (53.sp * 1.56f) // 156%
    ),
    bodyMedium500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp,
        lineHeight = (53.sp * 1.56f) // 156%
    ),
    bodyRegular500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 36.sp,
        lineHeight = (48.sp * 1.44f) // 144%
    ),
    bodyRegular400 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = (48.sp * 1.44f) // 144%
    ),
    bodySmall500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = (42.sp * 1.5f) // 150%
    ),
    bodySmall400 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = (42.sp * 1.5f) // 150%
    ),
    bodySmall300 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Light,
        fontSize = 32.sp,
        lineHeight = (42.sp * 1.5f) // 150%
    )
)

val AppTypography: Typography = Typography(
    // Display 헤더
    displayLarge  = SpotTypography.header01,
    displayMedium = SpotTypography.header02,
    displaySmall  = SpotTypography.header03,

    // Headline
    headlineLarge  = SpotTypography.header04,
    headlineMedium = SpotTypography.header05,

    // Title / Body / Label 매핑
    titleLarge  = SpotTypography.bodyLarge600,
    titleMedium = SpotTypography.bodyMedium600,
    titleSmall  = SpotTypography.bodySmall500,

    bodyLarge = SpotTypography.bodyLarge500,
    bodyMedium = SpotTypography.bodyMedium500,
    bodySmall = SpotTypography.bodySmall400,

    labelLarge = SpotTypography.bodyRegular500,
    labelMedium = SpotTypography.bodyRegular400,
    labelSmall = SpotTypography.bodySmall300
)