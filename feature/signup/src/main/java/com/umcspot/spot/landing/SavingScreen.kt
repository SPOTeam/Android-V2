package com.umcspot.spot.landing


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
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.GageBar
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import kotlinx.coroutines.delay

@Composable
fun SavingScreen(
    contentPadding: PaddingValues,
    autoProgress: Boolean = true,
    autoDurationMs: Int = 1800,
    blockBackPress: Boolean = true,
    onFinished: () -> Unit,
) {
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    BackHandler(enabled = blockBackPress) { /* no-op: 뒤로가기 무시 */ }

    val internal = remember { Animatable(0f) }
    var isDone by remember { mutableStateOf(false) }

    LaunchedEffect(autoProgress) {
        if (autoProgress) {
            internal.snapTo(0f)
            internal.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = autoDurationMs, easing = LinearEasing)
            )
            isDone = true
            delay(3000)
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad, start = 16.dp, end = 16.dp)
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
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "당신의 스터디 파트너 \n 스팟, SPOT",
                style = SpotTheme.typography.bodyMedium500.copy(fontSize = 20.sp),
                color = SpotTheme.colors.B500,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isDone) "등록 완료!" else "내 정보 저장 중..",
                style = SpotTheme.typography.bodySmall400.copy(fontSize = 13.sp),
                color = SpotTheme.colors.B500
            )
            Spacer(Modifier.height(8.dp))
            GageBar(
                value = internal.value,
            )
        }
    }
}
