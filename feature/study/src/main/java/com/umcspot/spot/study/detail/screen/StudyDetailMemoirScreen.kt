package com.umcspot.spot.study.detail.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
    onEmojiToggle: (Long, String) -> Unit,
    onDeleteMemoir: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        memoirs.forEachIndexed { index, memoir ->
            key(memoir.memoirId) {
                MemoirItemView(
                    memoir = memoir,
                    onEmojiClick = { type ->
                        onEmojiToggle(memoir.memoirId, type)
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
}

@Composable
fun MemoirItemView(
    memoir: MemoirModel,
    onEmojiClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isEmojiPopupVisible by remember { mutableStateOf(false) }
    var isDeleteMenuVisible by remember { mutableStateOf(false) }

    var line1 by remember { mutableIntStateOf(0) }
    var line2 by remember { mutableIntStateOf(0) }
    var line3 by remember { mutableIntStateOf(0) }

    val totalLines = line1 + line2 + line3
    val isOverflowing = totalLines > 9

    val formattedDate = remember(memoir.createdAt) {
        try {
            val dateTime = memoir.createdAt.split("T")
            val datePart = dateTime[0].replace("-", ".").substring(2)
            val timePart = dateTime[1].substring(0, 5)
            "$datePart $timePart"
        } catch (e: Exception) {
            memoir.createdAt
        }
    }

    // 매 recomposition마다 최신값 캡처
    val reactions = memoir.reactions
    val counts = memoir.reactionCounts

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(12.dp))
    ) {
        // --- 헤더 영역 ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(screenWidthDp(33.dp))
                        .clip(CircleShape)
                        .background(SpotTheme.colors.gray300)
                ) {
                    AsyncImage(
                        model = memoir.profileImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))
                Text(
                    text = memoir.nickname,
                    style = SpotTheme.typography.medium_400,
                    color = SpotTheme.colors.black
                )
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

        // --- 섹션 내용 영역 ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            MemoirSectionItem(
                label = "오늘 한 일",
                text = memoir.activity,
                maxLines = if (isExpanded) Int.MAX_VALUE else 7,
                onLineMeasured = { line1 = it }
            )

            if (isExpanded || line1 < 8) {
                MemoirSectionItem(
                    label = "새롭게 배운 점",
                    text = memoir.learned,
                    maxLines = if (isExpanded) Int.MAX_VALUE else (9 - line1).coerceAtLeast(1),
                    onLineMeasured = { line2 = it }
                )
            }

            if (isExpanded || (line1 + line2) < 8) {
                MemoirSectionItem(
                    label = "고생한 나에게 한 마디",
                    text = memoir.encouragement,
                    maxLines = if (isExpanded) Int.MAX_VALUE else (9 - line1 - line2).coerceAtLeast(1),
                    onLineMeasured = { line3 = it }
                )
            }

            if (isOverflowing) {
                Row(
                    modifier = Modifier
                        .padding(top = screenHeightDp(8.dp))
                        .noRippleClickable { isExpanded = !isExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down),
                        contentDescription = null,
                        modifier = Modifier.size(screenWidthDp(14.dp))
                    )
                    Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
                    Text(
                        text = if (isExpanded) "간략히" else "더보기",
                        style = SpotTheme.typography.small_400
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        // --- 푸터 영역 ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                // Popup 제거 → 일반 컴포저블로 recomposition 정상 반영
                if (isEmojiPopupVisible) {
                    EmojiOptionPopup(
                        states = listOf(
                            reactions.isFired,
                            reactions.isHearted,
                            reactions.isStarred,
                            reactions.isSmiled
                        ),
                        onToggle = { index ->
                            val type = when (index) {
                                0 -> "FIRE"
                                1 -> "HEART"
                                2 -> "STAR"
                                else -> "SMILE"
                            }
                            onEmojiClick(type)
                            isEmojiPopupVisible = false
                        }
                    )
                    Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = null,
                            modifier = Modifier
                                .size(screenWidthDp(14.dp))
                                .noRippleClickable { isEmojiPopupVisible = !isEmojiPopupVisible }
                        )

                        // + 바로 아래에 팝업 — Box 기준 BottomStart
                        if (isEmojiPopupVisible) {
                            Box(
                                modifier = Modifier
                                    .padding(top = screenWidthDp(20.dp))  // + 아이콘 높이만큼 아래로
                                    .align(Alignment.BottomStart)
                            ) {
                                EmojiOptionPopup(
                                    states = listOf(
                                        reactions.isFired,
                                        reactions.isHearted,
                                        reactions.isStarred,
                                        reactions.isSmiled
                                    ),
                                    onToggle = { index ->
                                        val type = when (index) {
                                            0 -> "FIRE"
                                            1 -> "HEART"
                                            2 -> "STAR"
                                            else -> "SMILE"
                                        }
                                        onEmojiClick(type)
                                        isEmojiPopupVisible = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                    Row(horizontalArrangement = Arrangement.spacedBy(screenWidthDp(5.dp))) {
                        EmojiBadge(
                            R.drawable.ic_fire,
                            counts.fireCount,
                            reactions.isFired
                        ) { onEmojiClick("FIRE") }
                        EmojiBadge(
                            R.drawable.ic_heart,
                            counts.heartCount,
                            reactions.isHearted
                        ) { onEmojiClick("HEART") }
                        EmojiBadge(
                            R.drawable.ic_star,
                            counts.starCount,
                            reactions.isStarred
                        ) { onEmojiClick("STAR") }
                        EmojiBadge(
                            R.drawable.ic_laugh,
                            counts.smileCount,
                            reactions.isSmiled
                        ) { onEmojiClick("SMILE") }
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
}