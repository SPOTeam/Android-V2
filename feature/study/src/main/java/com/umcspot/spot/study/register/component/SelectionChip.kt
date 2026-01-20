package com.umcspot.spot.study.register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun SelectionChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(screenHeightDp(35.dp))
            .background(
                color = if (isSelected) SpotTheme.colors.B100 else SpotTheme.colors.white,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = SpotTheme.colors.gray200,
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp))
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SpotTheme.typography.medium_500,
            color = if (isSelected) SpotTheme.colors.B500 else SpotTheme.colors.black,
            modifier = Modifier.padding(vertical = screenHeightDp(7.dp)),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}