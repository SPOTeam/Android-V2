package com.example.core.ui.component.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.R
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.B200
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.G400
import com.example.core.ui.theme.SpotTypography
import com.example.core.ui.theme.White

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
            style = SpotTypography.header05,
            fontSize = 30.sp,
            color = B500
        )
        Spacer(Modifier.height(15.dp))
        Text(
            text = alertDes,
            style = SpotTypography.header05,
            color = G400,
            fontSize = 25.sp,
        )
        Spacer(Modifier.height(40.dp))
        content() // ✅ 여기로 버튼 등 추가
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyAlertPreview() {
    EmptyAlert(
        modifier = Modifier.padding(10.dp),
        painter = painterResource( R.drawable.alert),
        alertTitle = "신청한 스터디가 아직 없어요!",
        alertDes = "스팟에서 내 목표를 이뤄봐요"
    )
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
        isPressed -> Triple(B500, B500, White) // 눌림: 파란 배경 + 흰 글자
        else -> Triple(White, B500, B500)      // 기본: 흰 배경 + 파란 글자/테두리
    }

    EmptyAlert(
        modifier = modifier,
        painter = painter,
        alertTitle = alertTitle,
        alertDes = alertDes
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .border( // ✅ 선 색상 & 두께
                    width = 1.dp,
                    color = borderColor,
                    shape = SpotShapes.Hard
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        onClick()
                    }
                ),
            shape = SpotShapes.Hard,
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 80.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buttonText,
                    style = SpotTypography.bodyMedium500,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyAlertWithButtonPreview() {
    EmptyAlertWithButton(
        modifier = Modifier.padding(10.dp),
        painter = painterResource( R.drawable.alert),
        alertTitle = "신청한 스터디가 아직 없어요!",
        alertDes = "스팟에서 내 목표를 이뤄봐요",
        buttonText = "스터디 둘러보기",
        onClick = {}
    )
}