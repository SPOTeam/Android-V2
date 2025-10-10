package com.umcspot.spot.designsystem.theme

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R

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

/**
 * SpotTheme가 CompositionLocal로 제공/갱신할 실제 타입.
 * var + update(other) 형태로 remember{copy()}/update() 패턴을 지원한다.
 */
data class SpotTypography(
    var header01: TextStyle,
    var header02: TextStyle,
    var header03: TextStyle,
    var header04: TextStyle,
    var header05: TextStyle,
    var bodyLarge600: TextStyle,
    var bodyLarge500: TextStyle,
    var bodyMedium600: TextStyle,
    var bodyMedium500: TextStyle,
    var bodyRegular500: TextStyle,
    var bodyRegular400: TextStyle,
    var bodySmall500: TextStyle,
    var bodySmall400: TextStyle,
    var bodySmall300: TextStyle,
) {
    fun update(other: SpotTypography) {
        header01 = other.header01
        header02 = other.header02
        header03 = other.header03
        header04 = other.header04
        header05 = other.header05
        bodyLarge600 = other.bodyLarge600
        bodyLarge500 = other.bodyLarge500
        bodyMedium600 = other.bodyMedium600
        bodyMedium500 = other.bodyMedium500
        bodyRegular500 = other.bodyRegular500
        bodyRegular400 = other.bodyRegular400
        bodySmall500 = other.bodySmall500
        bodySmall400 = other.bodySmall400
        bodySmall300 = other.bodySmall300
    }
}

/** 기본 SpotTypography 세트를 생성하는 팩토리 함수 */
fun SpotTypography(): SpotTypography = SpotTypography(
    header01 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 85.sp, // 64pt ≈ 85sp
    ),
    header02 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 69.sp, // 52pt ≈ 69sp
    ),
    header03 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp, // 48pt ≈ 64sp
    ),
    header04 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp, // 44pt ≈ 58sp
    ),
    header05 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp, // 40pt ≈ 53sp
    ),
    bodyLarge600 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 44.sp,
    ),
    bodyLarge500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 44.sp,
    ),
    bodyMedium600 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
    ),
    bodyMedium500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp,
    ),
    bodyRegular500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 36.sp,
    ),
    bodyRegular400 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
    ),
    bodySmall500 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
    ),
    bodySmall400 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
    ),
    bodySmall300 = TextStyle(
        fontFamily = Suit,
        fontWeight = FontWeight.Light,
        fontSize = 32.sp,
    )
)

/**
 * M3 Typography 매핑.
 * - Material 컴포넌트에서 사용할 수 있게 연결
 * - SpotTheme() 내부에서 typography = AppTypography 로 전달됨
 */
private val DefaultSpotTypography = SpotTypography()

val AppTypography: Typography = Typography(
    displayLarge  = DefaultSpotTypography.header01,
    displayMedium = DefaultSpotTypography.header02,
    displaySmall  = DefaultSpotTypography.header03,

    headlineLarge  = DefaultSpotTypography.header04,
    headlineMedium = DefaultSpotTypography.header05,

    titleLarge  = DefaultSpotTypography.bodyLarge600,
    titleMedium = DefaultSpotTypography.bodyMedium600,
    titleSmall  = DefaultSpotTypography.bodySmall500,

    bodyLarge = DefaultSpotTypography.bodyLarge500,
    bodyMedium = DefaultSpotTypography.bodyMedium500,
    bodySmall = DefaultSpotTypography.bodySmall400,

    labelLarge = DefaultSpotTypography.bodyRegular500,
    labelMedium = DefaultSpotTypography.bodyRegular400,
    labelSmall = DefaultSpotTypography.bodySmall300
)

/** 미리보기 */
@Composable
fun TypographyPreviewContent() {
    Surface {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            val t = SpotTheme.typography
            Text("Header01 - 64sp Bold", style = t.header01)
            Text("Header02 - 52sp Bold", style = t.header02)
            Text("Header03 - 48sp Bold", style = t.header03)
            Text("Header04 - 44sp Bold", style = t.header04)
            Text("Header05 - 40sp Bold", style = t.header05)

            Text("BodyLarge600 - 44sp SemiBold", style = t.bodyLarge600)
            Text("BodyLarge500 - 44sp Medium", style = t.bodyLarge500)
            Text("BodyMedium600 - 40sp SemiBold", style = t.bodyMedium600)
            Text("BodyMedium500 - 40sp Medium", style = t.bodyMedium500)
            Text("BodyRegular500 - 36sp Medium", style = t.bodyRegular500)
            Text("BodyRegular400 - 36sp Normal", style = t.bodyRegular400)
            Text("BodySmall500 - 32sp Medium", style = t.bodySmall500)
            Text("BodySmall400 - 32sp Normal", style = t.bodySmall400)
            Text("BodySmall300 - 32sp Light", style = t.bodySmall300)
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 3000)
@Composable
fun TypographyPreview() {
    SpotTheme {
        TypographyPreviewContent()
    }
}
