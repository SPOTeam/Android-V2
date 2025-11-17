package com.umcspot.spot.designsystem.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_thin, weight = FontWeight.W100),
    Font(R.font.pretendard_extralight, weight = FontWeight.W200),
    Font(R.font.pretendard_light, weight = FontWeight.W300),
    Font(R.font.pretendard_regular, weight = FontWeight.W400),
    Font(R.font.pretendard_medium, weight = FontWeight.W500),
    Font(R.font.pretendard_semibold, weight = FontWeight.W600),
    Font(R.font.pretendard_bold, weight = FontWeight.W700),
    Font(R.font.pretendard_extrabold, weight = FontWeight.W800),
)

private fun SpotTextStyle(
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit
): TextStyle = TextStyle(
    fontFamily = Pretendard,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

@Stable
class SpotTypography internal constructor(
    h1: TextStyle,
    h2: TextStyle,
    h3: TextStyle,
    h4: TextStyle,
    h5: TextStyle,
    large_500: TextStyle,
    large_400: TextStyle,
    medium_500: TextStyle,
    medium_400: TextStyle,
    regular_500: TextStyle,
    regular_400: TextStyle,
    small_500: TextStyle,
    small_400: TextStyle,
    small_300: TextStyle,
) {
    var h1 by mutableStateOf(h1)
        private set
    var h2 by mutableStateOf(h2)
        private set
    var h3 by mutableStateOf(h3)
        private set
    var h4 by mutableStateOf(h4)
        private set
    var h5 by mutableStateOf(h5)
        private set
    var large_500 by mutableStateOf(large_500)
        private set
    var large_400 by mutableStateOf(large_400)
        private set
    var medium_500 by mutableStateOf(medium_500)
        private set
    var medium_400 by mutableStateOf(medium_400)
        private set
    var regular_500 by mutableStateOf(regular_500)
        private set
    var regular_400 by mutableStateOf(regular_400)
        private set
    var small_500 by mutableStateOf(small_500)
        private set
    var small_400 by mutableStateOf(small_400)
        private set
    var small_300 by mutableStateOf(small_300)
        private set

    fun copy(): SpotTypography = SpotTypography(
        h1 = h1,
        h2 = h2,
        h3 = h3,
        h4 = h4,
        h5 = h5,
        large_500 = large_500,
        large_400 = large_400,
        medium_500 = medium_500,
        medium_400 = medium_400,
        regular_500 = regular_500,
        regular_400 = regular_400,
        small_500 = small_500,
        small_400 = small_400,
        small_300 = small_300,
    )

    fun update(other: SpotTypography) {
        h1 = other.h1
        h2 = other.h2
        h3 = other.h3
        h4 = other.h4
        h5 = other.h5
        large_500 = other.large_500
        large_400 = other.large_400
        medium_500 = other.medium_500
        medium_400 = other.medium_400
        regular_500 = other.regular_500
        regular_400 = other.regular_400
        small_500 = other.small_500
        small_400 = other.small_400
        small_300 = other.small_300
    }
}

@Composable
fun SpotTypography(): SpotTypography {

    val bodyLetterSpacing = (-0.022).em

    return SpotTypography(

        h1 = SpotTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = (24 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        h2 = SpotTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = (20 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        h3 = SpotTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = (18 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        h4 = SpotTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = (16 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        h5 = SpotTextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = (14 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        large_500 = SpotTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = (16 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        large_400 = SpotTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = (16 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        medium_500 = SpotTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = (14 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),
        medium_400 = SpotTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = (14 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        regular_500 = SpotTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = (12 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),
        regular_400 = SpotTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = (12 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),

        small_500 = SpotTextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            lineHeight = (10 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),
        small_400 = SpotTextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            lineHeight = (10 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        ),
        small_300 = SpotTextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 10.sp,
            lineHeight = (10 * 1.5).sp,
            letterSpacing = bodyLetterSpacing
        )
    )
}

@Composable
private fun DefaultSpotTypography() = SpotTypography()

val AppTypography: Typography
    @Composable
    get() = Typography(

        displayLarge = DefaultSpotTypography().h1,
        displayMedium = DefaultSpotTypography().h2,
        displaySmall = DefaultSpotTypography().h3,


        headlineLarge = DefaultSpotTypography().h4,
        headlineMedium = DefaultSpotTypography().h5,
        headlineSmall = DefaultSpotTypography().large_500,


        titleLarge = DefaultSpotTypography().large_500,
        titleMedium = DefaultSpotTypography().medium_500,
        titleSmall = DefaultSpotTypography().regular_500,


        bodyLarge = DefaultSpotTypography().large_400,
        bodyMedium = DefaultSpotTypography().medium_400,
        bodySmall = DefaultSpotTypography().regular_400,


        labelLarge = DefaultSpotTypography().regular_500,
        labelMedium = DefaultSpotTypography().small_500,
        labelSmall = DefaultSpotTypography().small_400
    )

@Composable
fun TypographyPreviewContent() {
    Surface {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            val t = SpotTheme.typography
            Text("H1 - 24sp Bold / 150%", style = t.h1)
            Text("H2 - 20sp Bold / 150%", style = t.h2)
            Text("H3 - 18sp Bold / 150%", style = t.h3)
            Text("H4 - 16sp Bold / 150%", style = t.h4)
            Text("H5 - 14sp Bold / 150%", style = t.h5)

            Text("--- (Letter Spacing: -2.2%) ---", style = t.regular_400)

            Text("Large 500 - 16sp Medium / 150%", style = t.large_500)
            Text("Large 400 - 16sp Normal / 150%", style = t.large_400)
            Text("Medium 500 - 14sp Medium / 150%", style = t.medium_500)
            Text("Medium 400 - 14sp Normal / 150%", style = t.medium_400)
            Text("Regular 500 - 12sp Medium / 150%", style = t.regular_500)
            Text("Regular 400 - 12sp Normal / 150%", style = t.regular_400)
            Text("Small 500 - 10sp Medium / 150%", style = t.small_500)
            Text("Small 400 - 10sp Normal / 150%", style = t.small_400)
            Text("Small 300 - 10sp Light / 150%", style = t.small_300)
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun TypographyPreview() {
    SpotTheme {
        TypographyPreviewContent()
    }
}