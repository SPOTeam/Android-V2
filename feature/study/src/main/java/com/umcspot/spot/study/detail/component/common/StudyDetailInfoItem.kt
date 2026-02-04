package com.umcspot.spot.study.detail.component.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyDetailInfoItem(
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .width(screenWidthDp(56.dp))
            .padding(
                horizontal = screenWidthDp(3.dp),
                vertical = screenHeightDp(1.5.dp)
            )
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(screenWidthDp(14.dp)),
            tint = SpotTheme.colors.black
        )
        Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
        Text(
            text = text,
            style = SpotTheme.typography.small_400,
            color = SpotTheme.colors.black,
            maxLines = 1
        )
    }
}