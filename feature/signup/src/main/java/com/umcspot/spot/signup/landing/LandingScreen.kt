package com.umcspot.spot.signup.landing

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.KakaoStartButton
import com.umcspot.spot.designsystem.component.button.NaverStartButton
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LandingRoute(
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LandingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is LandingSideEffect.NavigateToHome -> navigateToSignUp()
                is LandingSideEffect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = SpotTheme.colors.white,
        contentWindowInsets = WindowInsets.systemBars,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        LandingScreen(
            contentPadding = innerPadding,
            isLoading = uiState.isLoading,
            onKakaoClick = {
                if (!uiState.isLoading) {
                    activity?.let { act ->
                        viewModel.startSocialLogin(SocialLoginType.KAKAO, act)
                    }
                }
            },
            onNaverClick = {
                if (!uiState.isLoading) {
                    activity?.let { act ->
                        viewModel.startSocialLogin(SocialLoginType.NAVER, act)
                    }
                }
            }
        )
    }
}

@Composable
fun LandingScreen(
    contentPadding: PaddingValues,
    isLoading: Boolean,
    onKakaoClick: () -> Unit,
    onNaverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(contentPadding)
            .padding(horizontal = screenWidthDp(17.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                modifier = Modifier.size(screenWidthDp(33.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(screenHeightDp(52.dp)))
            Text(
                text = "당신의 스터디 파트너\n스팟, SPOT",
                style = SpotTheme.typography.h2,
                textAlign = TextAlign.Center,
                color = SpotTheme.colors.B500,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = screenHeightDp(63.dp)),
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(10.dp))
        ) {
            KakaoStartButton(onClick = onKakaoClick)
            NaverStartButton(onClick = onNaverClick)
        }
    }
}