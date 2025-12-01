package com.umcspot.spot.landing

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.KakaoStartButton
import com.umcspot.spot.designsystem.component.button.NaverStartButton
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.SocialLoginType

@Composable
fun LandingScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LandingViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as? Activity

    LaunchedEffect(Unit) {
        viewModel.events.collect { ev ->
            when (ev) {
                is LandingViewModel.LoginEvent.LoginSucceeded -> {
                    onLoginSuccess()
                }
                is LandingViewModel.LoginEvent.ShowError -> {
                }
            }
        }
    }

    LandingScreenContent(
        onKakaoClick = {
            activity?.let { act ->
                viewModel.startSocialLogin(SocialLoginType.KAKAO, act)
            }
        },
        onNaverClick = {
            activity?.let { act ->
                viewModel.startSocialLogin(SocialLoginType.NAVER, act)
            }
        },
    )
}

@Composable
fun LandingScreenContent(
    onKakaoClick: () -> Unit,
    onNaverClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        color = SpotTheme.colors.white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.spot_logo),
                    contentDescription = "SPOT 로고",
                    modifier = Modifier
                        .size(33.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(52.dp))
                Text(
                    text = "당신의 스터디 파트너\n스팟, SPOT",
                    textAlign = TextAlign.Center,
                    lineHeight = 45.sp,
                    color = SpotTheme.colors.B500,
                    style = SpotTheme.typography.small_400
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KakaoStartButton(onClick = onKakaoClick)
                NaverStartButton(onClick = onNaverClick)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LandingScreenPreview() {
    SpotTheme {
        LandingScreenContent(
            onKakaoClick = {},
            onNaverClick = {}
        )
    }
}
