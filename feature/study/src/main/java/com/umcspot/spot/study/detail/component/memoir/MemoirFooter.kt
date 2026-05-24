package com.umcspot.spot.study.detail.component.memoir

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun MemoirFooter(
    formattedDate: String,
    isFired: Boolean,
    isHearted: Boolean,
    isStarred: Boolean,
    isSmiled: Boolean,
    fireCount: Int,
    heartCount: Int,
    starCount: Int,
    smileCount: Int,
    isPrivate: Boolean,
    isEmojiPopupVisible: Boolean,
    onEmojiPopupToggle: () -> Unit,
    onEmojiClick: (String) -> Unit
) {
    val emojiList = listOf(
        Triple(R.drawable.ic_fire, fireCount, isFired),
        Triple(R.drawable.ic_heart, heartCount, isHearted),
        Triple(R.drawable.ic_star, starCount, isStarred),
        Triple(R.drawable.ic_laugh, smileCount, isSmiled),
    )
    val emojiTypes = listOf("FIRE", "HEART", "STAR", "SMILE")

    val density = LocalDensity.current
    val popupOffsetY = with(density) { screenHeightDp(18.dp).roundToPx() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(5.dp))
            ) {
                if (!isPrivate) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = null,
                        modifier = Modifier.noRippleClickable { onEmojiPopupToggle() }
                    )
                }
                emojiList.forEachIndexed { i, (icon, count, selected) ->
                    EmojiBadge(
                        iconRes = icon,
                        count = count,
                        isSelected = selected,
                        onClick = { if (!isPrivate) onEmojiClick(emojiTypes[i]) }
                    )
                }
            }

            if (!isPrivate && isEmojiPopupVisible) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(0, popupOffsetY),
                    onDismissRequest = { onEmojiPopupToggle() }
                ) {
                    EmojiOptionPopup(
                        states = listOf(isFired, isHearted, isStarred, isSmiled),
                        onToggle = { index ->
                            onEmojiClick(emojiTypes[index])
                            onEmojiPopupToggle()
                        }
                    )
                }
            }
        }

        Text(
            text = formattedDate,
            style = SpotTheme.typography.regular_400,
            color = SpotTheme.colors.gray400
        )
    }
}