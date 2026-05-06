package com.umcspot.spot.study.detail.component.memoir

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
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun MemoirExpandButton(
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = screenHeightDp(4.dp))
            .noRippleClickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down
            ),
            contentDescription = null,
            modifier = Modifier.size(screenWidthDp(14.dp)),
            tint = SpotTheme.colors.gray400
        )
        Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
        Text(
            text = if (isExpanded) "간략히" else "더보기",
            style = SpotTheme.typography.small_400,
            color = SpotTheme.colors.gray400
        )
    }
}