package com.umcspot.spot.study.detail.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.component.memoir.MemoirExpandButton
import com.umcspot.spot.study.detail.component.memoir.MemoirFooter
import com.umcspot.spot.study.detail.component.memoir.MemoirHeader
import com.umcspot.spot.study.detail.component.memoir.MemoirSectionItem
import com.umcspot.spot.study.detail.mapper.toFormattedMemoirDate
import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.ImmutableList

private const val MAX_COLLAPSED_LINES = 9

@Composable
fun StudyDetailMemoirScreen(
    studyId: Long,
    memoirs: ImmutableList<MemoirModel>,
    onEmojiToggle: (Long, String, Boolean) -> Unit,
    onDeleteMemoir: (Long) -> Unit
) {
    if (memoirs.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeightDp(300.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.document),
                    contentDescription = null,
                    modifier = Modifier.size(screenWidthDp(33.dp)),
                    tint = SpotTheme.colors.gray400
                )
                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                Text(
                    text = "아직 올라온 회고록이 없어요!",
                    style = SpotTheme.typography.medium_400,
                    color = SpotTheme.colors.gray400
                )
            }
        }
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        memoirs.forEachIndexed { index, memoir ->
            MemoirItemView(
                memoir = memoir,
                onEmojiClick = { type ->
                    val isCurrentlySelected = when (type) {
                        "FIRE" -> memoir.reactions.isFired
                        "HEART" -> memoir.reactions.isHearted
                        "STAR" -> memoir.reactions.isStarred
                        "SMILE" -> memoir.reactions.isSmiled
                        else -> false
                    }
                    onEmojiToggle(memoir.memoirId, type, isCurrentlySelected)
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
    onEmojiClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isEmojiPopupVisible by remember { mutableStateOf(false) }
    var isDeleteMenuVisible by remember { mutableStateOf(false) }
    var section1Lines by remember { mutableIntStateOf(0) }
    var section2Lines by remember { mutableIntStateOf(0) }
    var section3Lines by remember { mutableIntStateOf(0) }
    var everOverflowed by remember { mutableStateOf(false) }

    val section1TotalLines = section1Lines + 1
    val section2TotalLines = section2Lines + 1
    val section3TotalLines = section3Lines + 1
    val totalLines = section1TotalLines + section2TotalLines + section3TotalLines

    LaunchedEffect(totalLines) {
        if (totalLines > MAX_COLLAPSED_LINES) everOverflowed = true
    }

    val section1Max = MAX_COLLAPSED_LINES - 1
    val section2Max = (MAX_COLLAPSED_LINES - section1TotalLines - 1).coerceAtLeast(0)
    val section3Max = (MAX_COLLAPSED_LINES - section1TotalLines - section2TotalLines - 1).coerceAtLeast(0)

    val formattedDate = remember(memoir.createdAt) { memoir.createdAt.toFormattedMemoirDate() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(12.dp))
    ) {
        MemoirHeader(
            profileImageUrl = memoir.profileImageUrl,
            nickname = memoir.nickname,
            isMyMemoir = memoir.isMyMemoir,
            isDeleteMenuVisible = isDeleteMenuVisible,
            onDeleteMenuToggle = { isDeleteMenuVisible = !isDeleteMenuVisible },
            onDeleteClick = {
                isDeleteMenuVisible = false
                onDeleteClick()
            }
        )

        Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            MemoirSectionItem(
                label = "오늘 한 일",
                text = memoir.activity,
                maxLines = if (isExpanded) Int.MAX_VALUE else section1Max,
                onLineCountMeasured = { section1Lines = it }
            )
            MemoirSectionItem(
                label = "새롭게 배운 점",
                text = memoir.learned,
                maxLines = if (isExpanded) Int.MAX_VALUE else section2Max,
                onLineCountMeasured = { section2Lines = it }
            )
            MemoirSectionItem(
                label = "고생한 나에게 한 마디",
                text = memoir.encouragement,
                maxLines = if (isExpanded) Int.MAX_VALUE else section3Max,
                onLineCountMeasured = { section3Lines = it }
            )

            if (everOverflowed) {
                MemoirExpandButton(
                    isExpanded = isExpanded,
                    onToggle = { isExpanded = !isExpanded }
                )
            }
        }

        if (memoir.imageUrls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
            MemoirImageRow(imageUrls = memoir.imageUrls)
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        MemoirFooter(
            formattedDate = formattedDate,
            isFired = memoir.reactions.isFired,
            isHearted = memoir.reactions.isHearted,
            isStarred = memoir.reactions.isStarred,
            isSmiled = memoir.reactions.isSmiled,
            fireCount = memoir.reactionCounts.fireCount,
            heartCount = memoir.reactionCounts.heartCount,
            starCount = memoir.reactionCounts.starCount,
            smileCount = memoir.reactionCounts.smileCount,
            isEmojiPopupVisible = isEmojiPopupVisible,
            onEmojiPopupToggle = { isEmojiPopupVisible = !isEmojiPopupVisible },
            onEmojiClick = onEmojiClick
        )
    }
}

@Composable
private fun MemoirImageRow(imageUrls: List<String>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(8.dp)),
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(6.dp)),
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