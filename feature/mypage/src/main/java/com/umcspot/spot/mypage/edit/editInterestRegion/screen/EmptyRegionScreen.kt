package com.umcspot.spot.mypage.edit.editInterestRegion.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EmptyRegionScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = null,
            tint = SpotTheme.colors.gray500,
            modifier = Modifier.size(screenWidthDp(33.dp))
        )
        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))
        Text(
            text = "관심 지역을 하나 이상 설정해주세요.\n지역은 10개까지 설정 가능합니다.",
            style = SpotTheme.typography.h5,
            color = SpotTheme.colors.gray500,
            textAlign = TextAlign.Center
        )
    }
}