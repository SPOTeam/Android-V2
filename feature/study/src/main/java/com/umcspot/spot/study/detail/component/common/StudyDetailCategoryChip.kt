package com.umcspot.spot.study.detail.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyDetailCategoryChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = SpotTheme.colors.B100,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(
                horizontal = screenWidthDp(7.dp),
                vertical = screenHeightDp(4.dp)
            )
    ) {
        Text(
            text = text,
            style = SpotTheme.typography.small_500,
            color = SpotTheme.colors.black
        )
    }
}