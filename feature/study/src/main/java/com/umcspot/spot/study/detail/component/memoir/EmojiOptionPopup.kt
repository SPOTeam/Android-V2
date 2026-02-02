package com.umcspot.spot.study.detail.component.memoir

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EmojiOptionPopup(states: List<Boolean>, onToggle: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .size(width = screenWidthDp(89.dp), height = screenHeightDp(26.dp))
            .background(SpotTheme.colors.white, RoundedCornerShape(screenWidthDp(10.dp)))
            .border(1.dp, SpotTheme.colors.gray200, RoundedCornerShape(screenWidthDp(10.dp)))
            .padding(horizontal = screenWidthDp(6.dp), vertical = screenHeightDp(4.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icons = listOf(R.drawable.ic_fire, R.drawable.ic_heart, R.drawable.ic_star, R.drawable.ic_laugh)
        icons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(screenWidthDp(18.dp))
                    .clip(RoundedCornerShape(screenWidthDp(6.dp)))
                    .background(if (states[index]) SpotTheme.colors.B200 else Color.Transparent)
                    .noRippleClickable { onToggle(index) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(screenWidthDp(14.dp))
                )
            }
        }
    }
}

@Composable
fun MemoirSectionItem(label: String, text: String, maxLines: Int, onLineMeasured: (Int) -> Unit) {
    if (text.isEmpty() || maxLines <= 0) return
    Column(modifier = Modifier.padding(vertical = screenHeightDp(8.dp))) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(screenWidthDp(8.dp)).clip(CircleShape).background(B200))
            Spacer(modifier = Modifier.width(screenWidthDp(6.dp)))
            Text(text = label, style = SpotTheme.typography.regular_500, color = SpotTheme.colors.black)
        }
        Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))
        Text(
            text = text,
            style = SpotTheme.typography.medium_400,
            color = SpotTheme.colors.black,
            modifier = Modifier.fillMaxWidth(),
            maxLines = if (maxLines > 1) maxLines - 1 else 1,
            onTextLayout = { onLineMeasured(1 + it.lineCount) }
        )
    }
}

@Composable
fun EmojiBadge(iconRes: Int, count: Int, isSelected: Boolean, onClick: () -> Unit) {
    if (count <= 0 && !isSelected) return
    val visualIconSize = if (iconRes == R.drawable.ic_laugh) screenWidthDp(18.dp) else screenWidthDp(14.dp)
    val backgroundColor = if (isSelected) SpotTheme.colors.B200 else Color.Transparent

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(screenWidthDp(6.dp)))
            .background(backgroundColor)
            .noRippleClickable { onClick() }
            .padding(horizontal = screenWidthDp(4.dp), vertical = screenHeightDp(2.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(screenWidthDp(14.dp)), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(visualIconSize), contentScale = ContentScale.Fit)
        }
        Spacer(modifier = Modifier.width(screenWidthDp(2.dp)))
        Text(text = count.toString(), style = SpotTheme.typography.small_400, color = SpotTheme.colors.B500)
    }
}