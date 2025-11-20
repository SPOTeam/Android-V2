package com.umcspot.spot.designsystem.component.post

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.post.PostResult
import com.umcspot.spot.model.BoardType
import com.umcspot.spot.model.toSpotForm
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun PostListItem(
    item: PostResult,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(modifier = modifier.padding(10.dp)
    ) {
        Row(
            modifier = modifier.clickable(onClick = onClick),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.title,
                    style = SpotTheme.typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.content,
                    style = SpotTheme.typography.regular_400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Stat(
                        iconRes = R.drawable.thumb_up, count2 = item.likeNum
                    )
                    Stat(
                        iconRes = R.drawable.comment, count2 = item.commentNum
                    )
                    Stat(
                        iconRes = R.drawable.eye, count2 = item.viewNum
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = item.date,
                        style = SpotTheme.typography.small_400,
                        color = SpotTheme.colors.G400,
                    )

                    Text(
                        text = item.time,
                        style = SpotTheme.typography.small_400,
                        color = SpotTheme.colors.G400
                    )
                }
            }
        }
        Spacer(Modifier.padding(5.dp))

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            color = SpotTheme.colors.G300,
            thickness = 0.5.dp
        )
    }
}

@Composable
private fun Stat(
    @DrawableRes iconRes: Int,
    count1: Int = 0,
    count2: Int
) {
    fun cap(n: Int) = if (n >= 1000) "999+" else n.toString()
    val display = if (count1 != 0) "${cap(count1)}/${cap(count2)}" else cap(count2)

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(14.dp)
        )

        Text(text = display, style = SpotTheme.typography.small_400, color = SpotTheme.colors.B500)
    }
}

/* ============== Preview ============== */

@Preview(showBackground = true, widthDp = 500)
@Composable
private fun PostListItemPreview() {
    SpotTheme{
        PostListItem(
            item = PostResult(
                id = 1,
                label = BoardType.FREETALK,
                title = "Sample Study",
                content = "Sample Goal",
                likeNum = 1000,
                commentNum = 100,
                viewNum = 90,
                date = "25.1.15",
                time = "14:30"
            ),
            onClick = {}
        )
    }
}

