package com.umcspot.spot.study.detail.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
                    onEmojiClick = { type -> onEmojiToggle(memoir.memoirId, type) },
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

    // 9줄 초과 여부 — 각 섹션이 실제로 overflow 됐는지 추적
    var overflow1 by remember { mutableStateOf(false) }
    var overflow2 by remember { mutableStateOf(false) }
    var overflow3 by remember { mutableStateOf(false) }
    // 한 번이라도 overflow가 감지되면 유지 (isExpanded 후 false로 리셋 방지)
    var everOverflowed by remember { mutableStateOf(false) }

    val isOverflowing = overflow1 || overflow2 || overflow3
    if (isOverflowing) everOverflowed = true

    val formattedDate = remember(memoir.createdAt) {
        try {
            val dt = memoir.createdAt.split("T")
            val date = dt[0].replace("-", ".").substring(2)
            val time = dt[1].substring(0, 5)
            "$date $time"
        } catch (e: Exception) { memoir.createdAt }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(12.dp))
            // 팝업 외부 클릭 시 닫기
            .then(
                if (isEmojiPopupVisible)
                    Modifier.noRippleClickable { isEmojiPopupVisible = false }
                else Modifier
            )
    ) {
        // ── 헤더 ──────────────────────────────────────────
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
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))
                Text(
                    text = memoir.nickname,
                    style = SpotTheme.typography.medium_400,
                    color = SpotTheme.colors.black
                )
            }
            // 케밥 메뉴 — 내 회고록만
            if (memoir.isMyMemoir) {
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_meetball),
                        contentDescription = null,
                        modifier = Modifier
                            .size(screenWidthDp(20.dp))
                            .noRippleClickable { isDeleteMenuVisible = true }
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

        // ── 본문 섹션 ──────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            // 접힌 상태: 각 섹션 최대 3줄씩 → 합계 최대 9줄
            // 펼친 상태: 전부 표시
            MemoirSectionItem(
                label = "오늘 한 일",
                text = memoir.activity,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                onOverflowDetected = { overflow1 = it }
            )
            MemoirSectionItem(
                label = "새롭게 배운 점",
                text = memoir.learned,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                onOverflowDetected = { overflow2 = it }
            )
            MemoirSectionItem(
                label = "고생한 나에게 한 마디",
                text = memoir.encouragement,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                onOverflowDetected = { overflow3 = it }
            )

            // 더보기 / 간략히 버튼 — 한 번이라도 overflow면 표시 유지
            if (everOverflowed) {
                Row(
                    modifier = Modifier
                        .padding(top = screenHeightDp(4.dp))
                        .noRippleClickable { isExpanded = !isExpanded },
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
        }

        // ── 이미지 섹션 (있을 때만) ────────────────────────
        if (memoir.imageUrls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
            MemoirImageRow(imageUrls = memoir.imageUrls)
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        // ── 푸터 (이모지 + 날짜) ──────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이모지 영역 전체를 Box로 감싸 팝업 위치 기준점 설정
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(5.dp))
                ) {
                    // (+) 버튼
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = null,
                        modifier = Modifier
                            .size(screenWidthDp(14.dp))
                            .noRippleClickable { isEmojiPopupVisible = !isEmojiPopupVisible }
                    )
                    // 이모지 배지들 — reactions/counts는 memoir에서 직접 읽음 (낙관적 업데이트 즉시 반영)
                    val emojiList = listOf(
                        Triple(R.drawable.ic_fire,  memoir.reactionCounts.fireCount,  memoir.reactions.isFired),
                        Triple(R.drawable.ic_heart, memoir.reactionCounts.heartCount, memoir.reactions.isHearted),
                        Triple(R.drawable.ic_star,  memoir.reactionCounts.starCount,  memoir.reactions.isStarred),
                        Triple(R.drawable.ic_laugh, memoir.reactionCounts.smileCount, memoir.reactions.isSmiled),
                    )
                    val emojiTypes = listOf("FIRE", "HEART", "STAR", "SMILE")
                    emojiList.forEachIndexed { i, (icon, count, selected) ->
                        EmojiBadge(
                            iconRes = icon,
                            count = count,
                            isSelected = selected,
                            onClick = { onEmojiClick(emojiTypes[i]) }
                        )
                    }
                }

                // ✅ 팝업은 딱 한 곳 — (+) 버튼 위에 표시
                if (isEmojiPopupVisible) {
                    EmojiOptionPopup(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(y = (-screenHeightDp(34.dp))), // 팝업 높이만큼 위로
                        states = listOf(
                            memoir.reactions.isFired,
                            memoir.reactions.isHearted,
                            memoir.reactions.isStarred,
                            memoir.reactions.isSmiled
                        ),
                        onToggle = { index ->
                            onEmojiClick(listOf("FIRE", "HEART", "STAR", "SMILE")[index])
                            isEmojiPopupVisible = false
                        }
                    )
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

@Composable
private fun MemoirImageRow(imageUrls: List<String>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(8.dp)), // 상하 여백 추가
        // 이미지 사이의 간격을 6dp로 설정
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(6.dp)),
        // 마지막 이미지까지 스크롤했을 때 오른쪽 끝에 여백을 주어 잘리지 않게 함
        contentPadding = PaddingValues(horizontal = screenWidthDp(17.dp))
    ) {
        items(imageUrls) { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = screenWidthDp(140.dp), height = screenHeightDp(140.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(SpotTheme.colors.gray200)
            )
        }
    }
}