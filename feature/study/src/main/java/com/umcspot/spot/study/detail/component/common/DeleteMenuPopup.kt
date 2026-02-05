package com.umcspot.spot.study.detail.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun DeleteMenuPopup(onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .width(screenWidthDp(80.dp))
            .background(SpotTheme.colors.white, RoundedCornerShape(8.dp))
            .border(1.dp, SpotTheme.colors.gray200, RoundedCornerShape(8.dp))
            .noRippleClickable { onDelete() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "삭제하기", 
            style = SpotTheme.typography.regular_400, 
            color = Color.Red
        )
    }
}