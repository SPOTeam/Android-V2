package com.umcspot.spot.designsystem.component.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.ClickSurface
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.postList.PostResult
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.korean
import com.umcspot.spot.study.model.StudyPostResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun PostListItem(
    item: PostResult,
    modifier: Modifier = Modifier,
    onLikeClick: (PostResult) -> Unit = {},
    onClick: (PostResult) -> Unit = {},
) {
    ClickSurface(
        onClick = { onClick(item) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(12.dp), vertical = screenHeightDp(8.dp))
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = SpotTheme.typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "#" + item.postType.korean,
                    style = SpotTheme.typography.regular_400,
                    color = SpotTheme.colors.B500
                )
            }

            Spacer(Modifier.height(screenHeightDp(3.dp)))

            Text(
                text = item.content,
                style = SpotTheme.typography.regular_400,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier.height(screenHeightDp(7.dp)))


            Row {
                CountView(item = item, onLikeClick = onLikeClick)

                Spacer(Modifier.weight(1f))

                Text(
                    text = item.createdAt,
                    style = SpotTheme.typography.small_400,
                    color = SpotTheme.colors.G400
                )
            }
        }
    }

}

@Composable
fun PostListItem(
    item: StudyPostResult,
    modifier: Modifier = Modifier,
    onLikeClick: (StudyPostResult) -> Unit = {},
    onClick: (StudyPostResult) -> Unit = {},
) {
    ClickSurface(
        onClick = { onClick(item) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(12.dp), vertical = screenHeightDp(8.dp))
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = SpotTheme.typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.isPinned) {
                    Spacer(Modifier.width(screenWidthDp(4.dp)))

                    Image(
                        painter = painterResource(id = R.drawable.ic_pin),
                        modifier = Modifier.size(screenHeightDp(14.dp)),
                        contentDescription = null,
                    )
                }
            }

            Spacer(Modifier.height(screenHeightDp(3.dp)))

            Text(
                text = item.content,
                style = SpotTheme.typography.regular_400,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier.height(screenHeightDp(7.dp)))


            Row {
                CountView(item = item, onLikeClick = onLikeClick)

                Spacer(Modifier.weight(1f))

                Text(
                    text = item.createdAt,
                    style = SpotTheme.typography.small_400,
                    color = SpotTheme.colors.G400
                )
            }
        }
    }
}
