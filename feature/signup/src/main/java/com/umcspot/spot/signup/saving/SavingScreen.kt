package com.umcspot.spot.signup.saving

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.GageBar
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.delay

@Composable
fun SavingRoute(
    contentPadding: PaddingValues,
    navigateToHome: () -> Unit,
    autoDurationMs: Int = 1800,
    blockBackPress: Boolean = true
) {
    BackHandler(enabled = blockBackPress) { }

    val progressAnim = remember { Animatable(0f) }
    var isDone by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        progressAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = autoDurationMs, easing = LinearEasing)
        )
        isDone = true
        delay(3000)
        navigateToHome()
    }

    SavingScreen(
        contentPadding = contentPadding,
        progress = progressAnim.value,
        isDone = isDone
    )
}


@Composable
fun SavingScreen(
    contentPadding: PaddingValues,
    progress: Float,
    isDone: Boolean
) {
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(
                top = topPad,
                bottom = bottomPad,
                start = screenWidthDp(17.dp),
                end = screenWidthDp(17.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.spot_logo),
                contentDescription = null,
                modifier = Modifier.size(screenWidthDp(40.dp))
            )
            Spacer(Modifier.height(screenHeightDp(16.dp)))
            Text(
                text = "당신의 스터디 파트너 \n 스팟, SPOT",
                style = SpotTheme.typography.h2,
                color = SpotTheme.colors.B500,
                textAlign = TextAlign.Center
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = screenHeightDp(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isDone) "등록 완료 !" else "내 정보 저장 중..",
                style = SpotTheme.typography.h4,
                color = SpotTheme.colors.B500
            )
            Spacer(Modifier.height(screenHeightDp(12.dp)))

            GageBar(
                value = progress,
            )
        }
    }
}