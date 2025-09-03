package com.umcspot.spot.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
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
        fontSize = 85.sp, // 64pt ≈ 85sp
        lineHeight = (85.sp * 1.5f), // 150%
    ),
    header02 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 69.sp, // 52pt ≈ 69sp
        lineHeight = (69.sp * 1.54f), // 154%
    ),
    header03 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp, // 48pt ≈ 64sp
        lineHeight = (64.sp * 1.5f), // 150%
    ),
    header04 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 58.sp, // 44pt ≈ 58sp
        lineHeight = (58.sp * 1.54f), // 154%
    ),
    header05 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 53.sp, // 40pt ≈ 53sp
        lineHeight = (53.sp * 1.5f), // 150%
    ),
    bodyLarge600 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 58.sp,
        lineHeight = (58.sp * 1.46f), // 146%
        letterSpacing = (-0.02).em
    ),
    bodyLarge500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 58.sp,
        lineHeight = (58.sp * 1.46f), // 146%
        letterSpacing = (-0.02).em
    ),
    bodyMedium600 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 53.sp,
        lineHeight = (53.sp * 1.56f), // 156%
        letterSpacing = (-0.02).em
    ),
    bodyMedium500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 53.sp,
        lineHeight = (53.sp * 1.56f), // 156%
        letterSpacing = (-0.02).em
    ),
    bodyRegular500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 48.sp,
        lineHeight = (48.sp * 1.44f), // 144%
        letterSpacing = (-0.01).em
    ),
    bodyRegular400 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        lineHeight = (48.sp * 1.44f), // 144%
        letterSpacing = (-0.01).em
    ),
    bodySmall500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 42.sp,
        lineHeight = (42.sp * 1.5f), // 150%
        letterSpacing = (-0.01).em
    ),
    bodySmall400 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Normal, // 500 / 400 / 300
        fontSize = 42.sp,
        lineHeight = (42.sp * 1.5f), // 150%
        letterSpacing = (-0.01).em
    ),
    bodySmall300 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Light, // 500 / 400 / 300
        fontSize = 42.sp,
        lineHeight = (42.sp * 1.5f), // 150%
        letterSpacing = (-0.01).em
    )
)