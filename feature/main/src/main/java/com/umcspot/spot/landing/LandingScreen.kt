package com.umcspot.spot.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umcspot.spot.alert.navigation.Alert
import com.umcspot.spot.alert.navigation.AppliedAlert
import com.umcspot.spot.alert.navigation.navigateToAlert
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.FloatingMultipleButton
import com.umcspot.spot.designsystem.component.FloatingToUpButton
import com.umcspot.spot.designsystem.component.KakaoLoginButton
import com.umcspot.spot.designsystem.component.KakaoStartButton
import com.umcspot.spot.designsystem.component.NaverStartButton
import com.umcspot.spot.designsystem.component.appBar.AppBarHome
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.main.MainNavHost
import com.umcspot.spot.main.MainNavTab
import com.umcspot.spot.main.MainNavigator
import com.umcspot.spot.main.component.MainBottomBar
import com.umcspot.spot.main.rememberMainNavigator
import kotlinx.collections.immutable.toImmutableList

@Composable
fun LandingScreen(
    onKakaoClick : () -> Unit,
    onNaverClick : () -> Unit
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
            // 상단 여백 + 중앙 컨텐츠
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

            // 하단 액션 버튼들
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KakaoStartButton(
                    onClick = onKakaoClick
                )
                NaverStartButton(
                    onClick = onNaverClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    SpotTheme {
        LandingScreen(
            onKakaoClick = {},
            onNaverClick = {}
        )
    }
}
