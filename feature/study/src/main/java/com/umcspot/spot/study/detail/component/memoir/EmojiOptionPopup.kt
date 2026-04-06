package com.umcspot.spot.study.detail.component.memoir

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EmojiOptionPopup(
    modifier: Modifier = Modifier,
    states: List<Boolean>,
    onToggle: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .size(width = screenWidthDp(89.dp), height = screenHeightDp(26.dp))
            .background(SpotTheme.colors.white, RoundedCornerShape(screenWidthDp(10.dp)))
            .border(1.dp, SpotTheme.colors.gray200, RoundedCornerShape(screenWidthDp(10.dp)))
            .padding(horizontal = screenWidthDp(6.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icons = listOf(R.drawable.ic_fire, R.drawable.ic_heart, R.drawable.ic_star, R.drawable.ic_laugh)
        icons.forEachIndexed { index, icon ->
            Box(
                modifier = Modifier
                    .size(screenWidthDp(18.dp))
                    .clip(RoundedCornerShape(screenWidthDp(6.dp)))
                    // 선택된 이모지 배경색 (B200 사용)
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
fun MemoirSectionItem(
    label: String,
    text: String,
    maxLines: Int,
    onOverflowDetected: (Boolean) -> Unit
) {
    if (text.isBlank()) return // 비어있으면 렌더링 X

    Column(modifier = Modifier.padding(vertical = screenHeightDp(6.dp))) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(screenWidthDp(8.dp))
                    .clip(CircleShape)
                    .background(SpotTheme.colors.B200) // 디자인 시스템 컬러 사용
            )
            Spacer(modifier = Modifier.width(screenWidthDp(6.dp)))
            Text(
                text = label,
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.black
            )
        }
        Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))
        Text(
            text = text,
            style = SpotTheme.typography.medium_400,
            color = SpotTheme.colors.black,
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                // 실제 텍스트가 잘렸는지 여부를 상위로 전달
                onOverflowDetected(result.hasVisualOverflow)
            }
        )
    }
}

@Composable
fun EmojiBadge(
    iconRes: Int,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 💡 개수가 0이고 선택도 안 된 상태면 아예 보여주지 않음 (디자인 규칙)
    if (count <= 0 && !isSelected) return

    val visualIconSize = if (iconRes == R.drawable.ic_laugh) screenWidthDp(18.dp) else screenWidthDp(14.dp)
    // 선택 여부에 따른 배경색 (B200)
    val backgroundColor = if (isSelected) SpotTheme.colors.B200 else Color.Transparent

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(screenWidthDp(6.dp)))
            .background(backgroundColor)
            .noRippleClickable { onClick() }
            .padding(horizontal = screenWidthDp(4.dp), vertical = screenHeightDp(2.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(screenWidthDp(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(visualIconSize),
                contentScale = ContentScale.Fit
            )
        }

        // 💡 숫자가 0보다 클 때만 간격과 텍스트 노출
        if (count > 0) {
            Spacer(modifier = Modifier.width(screenWidthDp(2.dp)))
            Text(
                text = count.toString(),
                style = SpotTheme.typography.small_400,
                color = SpotTheme.colors.B500 // 강조 컬러
            )
        }
    }
}