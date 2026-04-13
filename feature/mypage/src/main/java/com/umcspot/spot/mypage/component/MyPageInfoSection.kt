package com.umcspot.spot.mypage.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun LoginType(type: SocialLoginType) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(screenWidthDp(10.dp))
    ) {
        Text(text = "소셜로그인", style = SpotTheme.typography.medium_500)
        Spacer(modifier = Modifier.height(screenHeightDp(1.dp)))
        Text(
            text = type.toLoginTypeText(),
            color = SpotTheme.colors.G400,
            style = SpotTheme.typography.medium_500
        )
    }
}

@Composable
fun EmailInfo(email: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(screenWidthDp(10.dp))
    ) {
        Text(text = "이메일", style = SpotTheme.typography.medium_500)
        Spacer(modifier = Modifier.height(screenHeightDp(1.dp)))
        Text(
            text = email,
            color = SpotTheme.colors.G400,
            style = SpotTheme.typography.medium_500
        )
    }
}

@Composable
fun AppVersion(appVersion: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(screenWidthDp(10.dp))
    ) {
        Text(text = "앱 버전", style = SpotTheme.typography.medium_500)
        Spacer(modifier = Modifier.height(screenHeightDp(1.dp)))
        Text(
            text = "v$appVersion",
            color = SpotTheme.colors.G400,
            style = SpotTheme.typography.medium_500
        )
    }
}

@Composable
fun SocialLoginType.toLoginTypeText(): String = when (this) {
    SocialLoginType.KAKAO -> "KakaoTalk"
    SocialLoginType.NAVER -> "Naver"
}