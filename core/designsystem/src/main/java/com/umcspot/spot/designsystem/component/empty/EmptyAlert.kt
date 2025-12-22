package com.umcspot.spot.designsystem.component.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EmptyAlert(
    modifier: Modifier = Modifier,
    painter : Painter,
    alertTitle : String,
    alertDes : String,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(SpotTheme.colors.white),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(G300),
            modifier = Modifier.size(screenWidthDp(27.dp))
        )
        Spacer(Modifier.height(screenHeightDp(20.dp)))
        Text(
            text = alertTitle,
            style = SpotTheme.typography.h3,
            fontSize = 30.sp,
            color = SpotTheme.colors.B500,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(screenHeightDp(4.dp)))
        Text(
            text = alertDes,
            style = SpotTheme.typography.large_500,
            color = SpotTheme.colors.G400,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(screenHeightDp(53.dp)))
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
    EmptyAlert(
        modifier = modifier,
        painter = painter,
        alertTitle = alertTitle,
        alertDes = alertDes
    ) {
        TextButton(
            modifier = Modifier
                .width(screenWidthDp(156.dp))
                .height(screenHeightDp(39.dp)),
            text = buttonText,
            style = SpotTheme.typography.h5,
            shape = SpotShapes.Soft,
            onClick = onClick,
            state = TextButtonState.B500State
        )
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
            alertDes = "스팟에서 내 목표를 이뤄봐요.",
            buttonText = "스터디 둘러보기",
            onClick = {}
        )
    }
}