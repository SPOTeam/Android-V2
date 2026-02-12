package com.umcspot.spot.designsystem.component.post

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.postList.PostResult
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.study.model.StudyPostResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun CountView(
    item: PostResult,
    onLikeClick: (PostResult) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(3.dp))
    ) {
        Stat(
            iconRes = R.drawable.thumb_up,
            count2 = item.likeNum,
            likeChecked = item.isLiked,
            onLikeClick = { onLikeClick(item) }
        )
        Stat(iconRes = R.drawable.comment, count2 = item.commentNum)
        Stat(iconRes = R.drawable.eye,     count2 = item.viewNum)
    }
}

@Composable
fun CountView(
    item: PostDetailResult,
    onLikeClick: (PostDetailResult) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(3.dp))
    ) {
        Stat(
            iconRes = R.drawable.thumb_up,
            count2 = item.likeCount,
            likeChecked = item.isLiked,
            onLikeClick = { onLikeClick(item) }
        )
        Stat(iconRes = R.drawable.comment, count2 = item.commentCount)
        Stat(iconRes = R.drawable.eye,     count2 = item.viewCount)
    }
}

@Composable
fun CountView(
    item: StudyPostResult,
    onLikeClick: (StudyPostResult) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(3.dp))
    ) {
        Stat(
            iconRes = R.drawable.thumb_up,
            count2 = item.likeCount.toLong(),
            likeChecked = item.isLiked,
            onLikeClick = { onLikeClick(item) }
        )
        Stat(iconRes = R.drawable.comment, count2 = item.commentCount.toLong())
        Stat(iconRes = R.drawable.eye, count2 = item.viewCount.toLong())
    }
}


@Composable
private fun Stat(
    @DrawableRes iconRes: Int,
    count1: Long = 0,
    count2: Long,
    likeChecked: Boolean = false,
    onLikeClick: () -> Unit = {}
) {
    fun cap(n: Long) = if (n >= 1000) "999+" else n.toString()
    val display = if (count1.toInt() != 0) "${cap(count1)}/${cap(count2)}" else cap(count2)

    val interaction = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .width(screenWidthDp(55.dp))
            .height(screenHeightDp(17.dp))
            .padding(horizontal = screenWidthDp(3.dp), vertical = screenWidthDp(1.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp))
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = if (likeChecked) SpotTheme.colors.B500 else Color.Unspecified,
            modifier = Modifier
                .size(14.dp)
                .clickable(
                    indication = null,
                    onClick = onLikeClick,
                    interactionSource = interaction
                )
        )

        Text(text = display, style = SpotTheme.typography.small_400, color = SpotTheme.colors.B500)
    }
}

@Preview(showBackground = true)
@Composable
private fun preview() {
    SpotTheme {
        CountView(
            item = PostResult.dummyPost(5, 10),
            onLikeClick = {}
        )
    }
}
