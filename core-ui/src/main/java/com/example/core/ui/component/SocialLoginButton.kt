package com.example.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.R
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.KakaoText
import com.example.core.ui.theme.KakaoYellow
import com.example.core.ui.theme.NaverGreen
import com.example.core.ui.theme.SpotTypography
import com.example.core.ui.theme.White

// ===== 공통 베이스 버튼 =====
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
        shape = SpotShapes.Hard,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // 아이콘: 항상 왼쪽 정렬
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterStart),
                contentScale = ContentScale.FillWidth
            )

            // 텍스트: 항상 가운데 정렬
            Text(
                text = text,
                style = SpotTypography.bodyMedium600,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

// ===== 4가지 래퍼 =====

// 1) 카카오 – “카카오톡으로 3초 시작하기”
@Composable
fun KakaoStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "카카오톡으로 3초 시작하기",
    enabled: Boolean = true
) = SocialSignButton(
    modifier = modifier,
    text = text,
    iconRes = R.drawable.kakaotalk, // 고정
    backgroundColor = KakaoYellow,
    contentColor = KakaoText,
    onClick = onClick,
    enabled = enabled
)

// 2) 네이버 – “네이버로 시작하기”
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
    backgroundColor = NaverGreen,
    contentColor = White,
    onClick = onClick,
    enabled = enabled
)

// 3) 카카오 – “카카오톡 로그인”
@Composable
fun KakaoLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "카카오톡 로그인",
    enabled: Boolean = true
) = SocialSignButton(
    modifier = modifier,
    text = text,
    iconRes = R.drawable.kakaotalk,
    backgroundColor = KakaoYellow,
    contentColor = KakaoText,
    onClick = onClick,
    enabled = enabled
)

// 4) 네이버 – “네이버 로그인”
@Composable
fun NaverLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "네이버 로그인",
    enabled: Boolean = true
) = SocialSignButton(
    modifier = modifier,
    text = text,
    iconRes = R.drawable.naver,
    backgroundColor = NaverGreen,
    contentColor = White,
    onClick = onClick,
    enabled = enabled
)

/* =================== Previews =================== */

@Preview(showBackground = true, widthDp = 360)
@Composable
fun KakaoStartButtonPreview() {
    KakaoStartButton(
        modifier = Modifier.padding(10.dp),
        onClick = {}
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun NaverStartButtonPreview() {
    NaverStartButton(
        modifier = Modifier.padding(10.dp),
        onClick = {}
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun KakaoLoginButtonPreview() {
    KakaoLoginButton (
        modifier = Modifier.padding(10.dp),
        onClick = {})
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun NaverLoginButtonPreview() {
    NaverLoginButton(
        modifier = Modifier.padding(10.dp),
        onClick = {}
    )
}
