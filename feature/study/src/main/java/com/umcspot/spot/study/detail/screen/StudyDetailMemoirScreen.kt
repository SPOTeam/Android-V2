package com.umcspot.spot.study.detail.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.component.common.DeleteMenuPopup
import com.umcspot.spot.study.detail.component.memoir.EmojiBadge
import com.umcspot.spot.study.detail.component.memoir.EmojiOptionPopup
import com.umcspot.spot.study.detail.component.memoir.MemoirSectionItem
import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyDetailMemoirScreen(
    studyId: Long,
    memoirs: List<MemoirModel>,
    onEmojiToggle: (Long, String, Boolean) -> Unit,
    onDeleteMemoir: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        memoirs.forEachIndexed { index, memoir ->
            MemoirItemView(
                memoir = memoir,
                onEmojiClick = { type, isSelected ->
                    onEmojiToggle(memoir.memoirId, type, isSelected)
                },
                onDeleteClick = { onDeleteMemoir(memoir.memoirId) }
            )
            if (index < memoirs.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = SpotTheme.colors.gray300
                )
            }
        }
    }
}

@Composable
fun MemoirItemView(
    memoir: MemoirModel,
    onEmojiClick: (String, Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isEmojiPopupVisible by remember { mutableStateOf(false) }
    var isDeleteMenuVisible by remember { mutableStateOf(false) }

    val activityLines = remember { mutableIntStateOf(0) }
    val learnedLines = remember { mutableIntStateOf(0) }
    val encouragementLines = remember { mutableIntStateOf(0) }
    val isOverflowing = (activityLines.intValue + learnedLines.intValue + encouragementLines.intValue) >= 9

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(12.dp))
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(screenWidthDp(33.dp)).clip(CircleShape).background(SpotTheme.colors.gray300)) {
                    AsyncImage(model = memoir.profileImageUrl, contentDescription = null, contentScale = ContentScale.Crop)
                }
                Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))
                Text(text = memoir.nickname, style = SpotTheme.typography.medium_400, color = SpotTheme.colors.black)
            }

            if (memoir.isMyMemoir) {
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_meetball),
                        contentDescription = null,
                        modifier = Modifier.noRippleClickable { isDeleteMenuVisible = true }
                    )
                    if (isDeleteMenuVisible) {
                        Popup(
                            alignment = Alignment.TopEnd,
                            offset = IntOffset(0, 70),
                            onDismissRequest = { isDeleteMenuVisible = false }
                        ) {
                            DeleteMenuPopup(onDelete = {
                                isDeleteMenuVisible = false
                                onDeleteClick()
                            })
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

        Column(modifier = Modifier.fillMaxWidth()) {
            MemoirSectionItem("오늘 한 일", memoir.activity, if (!isExpanded && isOverflowing) 8 else Int.MAX_VALUE) { activityLines.intValue = it }
            if (isExpanded || activityLines.intValue < 8) {
                MemoirSectionItem("새롭게 배운 점", memoir.learned, if (!isExpanded && isOverflowing) (8 - activityLines.intValue) else Int.MAX_VALUE) { learnedLines.intValue = it }
            }
            if (isExpanded || (activityLines.intValue + learnedLines.intValue) < 8) {
                MemoirSectionItem("고생한 나에게 한 마디", memoir.encouragement, if (!isExpanded && isOverflowing) (8 - (activityLines.intValue + learnedLines.intValue)) else Int.MAX_VALUE) { encouragementLines.intValue = it }
            }

            if (isOverflowing) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = screenHeightDp(8.dp)).noRippleClickable { isExpanded = !isExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down),
                        contentDescription = null,
                        modifier = Modifier.size(screenWidthDp(14.dp)),
                        tint = SpotTheme.colors.black
                    )
                    Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
                    Text(text = if (isExpanded) "간략히" else "더보기", style = SpotTheme.typography.small_400, color = SpotTheme.colors.black)
                }
            }

            if (!memoir.imageUrl.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(screenWidthDp(8.dp))) {
                    items(listOf(memoir.imageUrl)) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier.size(screenWidthDp(140.dp)).clip(RoundedCornerShape(screenWidthDp(6.dp))).background(SpotTheme.colors.gray100),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = null,
                        modifier = Modifier.size(screenWidthDp(20.dp)).noRippleClickable { isEmojiPopupVisible = true },
                        tint = SpotTheme.colors.black
                    )

                    if (isEmojiPopupVisible) {
                        Popup(
                            alignment = Alignment.BottomStart,
                            offset = IntOffset(0, 85),
                            onDismissRequest = { isEmojiPopupVisible = false }
                        ) {
                            EmojiOptionPopup(
                                states = listOf(memoir.reactions.isFired, memoir.reactions.isHearted, memoir.reactions.isStarred, memoir.reactions.isSmiled),
                                onToggle = { index ->
                                    val (type, isSelected) = when (index) {
                                        0 -> "FIRE" to memoir.reactions.isFired
                                        1 -> "HEART" to memoir.reactions.isHearted
                                        2 -> "STAR" to memoir.reactions.isStarred
                                        else -> "SMILE" to memoir.reactions.isSmiled
                                    }
                                    onEmojiClick(type, isSelected)
                                    isEmojiPopupVisible = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(screenWidthDp(5.dp)))
                EmojiBadge(R.drawable.ic_fire, memoir.reactionCounts.fireCount, memoir.reactions.isFired) { onEmojiClick("FIRE", memoir.reactions.isFired) }
                Spacer(modifier = Modifier.width(screenWidthDp(5.dp)))
                EmojiBadge(R.drawable.ic_heart, memoir.reactionCounts.heartCount, memoir.reactions.isHearted) { onEmojiClick("HEART", memoir.reactions.isHearted) }
                Spacer(modifier = Modifier.width(screenWidthDp(5.dp)))
                EmojiBadge(R.drawable.ic_star, memoir.reactionCounts.starCount, memoir.reactions.isStarred) { onEmojiClick("STAR", memoir.reactions.isStarred) }
                Spacer(modifier = Modifier.width(screenWidthDp(5.dp)))
                EmojiBadge(R.drawable.ic_laugh, memoir.reactionCounts.smileCount, memoir.reactions.isSmiled) { onEmojiClick("SMILE", memoir.reactions.isSmiled) }
            }

            Text(text = memoir.createdAt, style = SpotTheme.typography.regular_400, color = SpotTheme.colors.gray400)
        }
    }
}