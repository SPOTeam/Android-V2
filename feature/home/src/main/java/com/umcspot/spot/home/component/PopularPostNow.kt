package com.umcspot.spot.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.component.button.ImageButtonState
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.board.BestPostResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun PopularPostNow(
    postInfo: BestPostResult,
    onCardClick: () -> Unit,
    onContentClick: (Long) -> Unit,
) {
    BlankButton(
        modifier = Modifier
            .width(screenWidthDp(156.dp))
            .height(screenHeightDp(79.dp)),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = screenWidthDp(8.dp))
                .matchParentSize(),
            verticalArrangement = Arrangement.spacedBy(
                screenHeightDp(7.dp),
                Alignment.CenterVertically
            )
        ) {
            Row(
                modifier = Modifier.padding(start = screenWidthDp(4.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "실시간 인기글",
                    style = SpotTheme.typography.h5,
                    color = Black
                )
                Box {
                    Image(
                        painter = painterResource(R.drawable.fire),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            SpotTheme.colors.black.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier
                            .size(screenWidthDp(20.dp))
                            .graphicsLayer {
                                translationY = 1.dp.toPx()
                                renderEffect = BlurEffect(12f, 12f)
                                clip = false
                            }
                    )
                    Image(
                        painter = painterResource(R.drawable.fire),
                        contentDescription = null,
                        modifier = Modifier.size(screenWidthDp(20.dp))
                    )
                }
            }

            BlankButton(
                modifier = Modifier
                    .width(screenWidthDp(140.dp))
                    .height(screenHeightDp(22.dp)),
                state = ImageButtonState.XOUTLINETransparentState,
                onClick = { onContentClick(postInfo.postId) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = screenWidthDp(4.dp), vertical = screenHeightDp(2.dp))
                        .matchParentSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = postInfo.title,
                        style = SpotTheme.typography.regular_500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Black,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = null,
                        tint = SpotTheme.colors.B500,
                        modifier = Modifier.size(screenWidthDp(17.dp))
                    )
                }
            }
        }
    }
}