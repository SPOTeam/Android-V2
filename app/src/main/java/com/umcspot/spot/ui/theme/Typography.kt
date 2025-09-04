package com.umcspot.spot.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.R

val Suit = FontFamily(
    Font(R.font.suit_thin, weight = FontWeight.W100),
    Font(R.font.suit_extralight, weight = FontWeight.W200),
    Font(R.font.suit_light, weight = FontWeight.W300),
    Font(R.font.suit_regular, weight = FontWeight.W400),
    Font(R.font.suit_medium, weight = FontWeight.W500),
    Font(R.font.suit_semibold, weight = FontWeight.W600),
    Font(R.font.suit_bold, weight = FontWeight.W700),
    Font(R.font.suit_extrabold, weight = FontWeight.W800),
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

@Composable
fun TypographyPreview() {
    Surface {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Header01 - 64sp Bold", style = SpotTypography.header01)
            Text("Header02 - 52sp Bold", style = SpotTypography.header02)
            Text("Header03 - 48sp Bold", style = SpotTypography.header03)
            Text("Header04 - 44sp Bold", style = SpotTypography.header04)
            Text("Header05 - 40sp Bold", style = SpotTypography.header05)

            Text("BodyLarge600 - 44sp SemiBold", style = SpotTypography.bodyLarge600)
            Text("BodyLarge500 - 44sp Medium", style = SpotTypography.bodyLarge500)
            Text("BodyMedium600 - 40sp SemiBold", style = SpotTypography.bodyMedium600)
            Text("BodyMedium500 - 40sp Medium", style = SpotTypography.bodyMedium500)
            Text("BodyRegular500 - 36sp Medium", style = SpotTypography.bodyRegular500)
            Text("BodyRegular400 - 36sp Normal", style = SpotTypography.bodyRegular400)
            Text("BodySmall500 - 32sp Medium", style = SpotTypography.bodySmall500)
            Text("BodySmall400 - 32sp Normal", style = SpotTypography.bodySmall400)
            Text("BodySmall300 - 32sp Light", style = SpotTypography.bodySmall300)
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 3000)
@Composable
fun TypographyPreviewPreview() {
    TypographyPreview()
}