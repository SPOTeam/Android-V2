package com.umcspot.spot.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.KakaoText
import com.umcspot.spot.designsystem.theme.KakaoYellow
import com.umcspot.spot.designsystem.theme.NaverGreen
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
private fun SocialSignButton(
    modifier: Modifier = Modifier,
    text: String,
    iconRes: Int,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = SpotShapes.Soft,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(
            horizontal = screenWidthDp(13.dp),
            vertical = screenHeightDp(13.dp)
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart),
                contentScale = ContentScale.FillWidth
            )

            Text(
                text = text,
                style = SpotTheme.typography.h4,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun KakaoStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "카카오톡으로 시작하기",
    enabled: Boolean = true
) = SocialSignButton(
    modifier = modifier,
    text = text,
    iconRes = R.drawable.kakaotalk,
    backgroundColor = SpotTheme.colors.KakaoYellow,
    contentColor = SpotTheme.colors.KakaoText,
    onClick = onClick,
    enabled = enabled
)

@Composable
fun NaverStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "네이버로 시작하기",
    enabled: Boolean = true
) = SocialSignButton(
    modifier = modifier,
    text = text,
    iconRes = R.drawable.naver,
    backgroundColor = SpotTheme.colors.NaverGreen,
    contentColor = White,
    onClick = onClick,
    enabled = enabled
)