package com.umcspot.spot.designsystem.component.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.ButtonState
import com.umcspot.spot.designsystem.component.button.TextButtonXL
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White

@Composable
fun EmptyAlert(
    modifier: Modifier = Modifier,
    painter : Painter,
    alertTitle : String,
    alertDes : String,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(G300),
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = alertTitle,
            style = SpotTheme.typography.header05,
            fontSize = 30.sp,
            color = SpotTheme.colors.B500
        )
        Spacer(Modifier.height(15.dp))
        Text(
            text = alertDes,
            style = SpotTheme.typography.header05,
            color = SpotTheme.colors.G400,
            fontSize = 25.sp,
        )
        Spacer(Modifier.height(40.dp))
        content() // ✅ 여기로 버튼 등 추가
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyAlertPreview() {
    SpotTheme {
        EmptyAlert(
            modifier = Modifier.padding(10.dp),
            painter = painterResource(R.drawable.alert),
            alertTitle = "신청한 스터디가 아직 없어요!",
            alertDes = "스팟에서 내 목표를 이뤄봐요"
        )
    }
}

@Composable
fun EmptyAlertWithButton(
    modifier: Modifier = Modifier,
    painter : Painter,
    alertTitle : String,
    alertDes : String,
    buttonText : String,
    onClick : () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (backgroundColor, borderColor, textColor) = when {
        isPressed -> Triple(SpotTheme.colors.B500, SpotTheme.colors.B500, SpotTheme.colors.white) // 눌림: 파란 배경 + 흰 글자
        else -> Triple(SpotTheme.colors.white, SpotTheme.colors.B500, SpotTheme.colors.B500)      // 기본: 흰 배경 + 파란 글자/테두리
    }

    EmptyAlert(
        modifier = modifier,
        painter = painter,
        alertTitle = alertTitle,
        alertDes = alertDes
    ) {
        TextButtonXL(text = buttonText, onClick = {}, state = ButtonState.B400State)
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyAlertWithButtonPreview() {
    SpotTheme {
        EmptyAlertWithButton(
            modifier = Modifier.padding(10.dp),
            painter = painterResource( R.drawable.alert),
            alertTitle = "신청한 스터디가 아직 없어요!",
            alertDes = "스팟에서 내 목표를 이뤄봐요",
            buttonText = "스터디 둘러보기",
            onClick = {}
        )
    }
}