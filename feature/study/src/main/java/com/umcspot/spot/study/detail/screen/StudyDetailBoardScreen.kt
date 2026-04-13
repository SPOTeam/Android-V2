package com.umcspot.spot.study.detail.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.post.PostListItem
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyPostResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyDetailBoardScreen(
    posts: List<StudyPostResult>,
    isLoading: Boolean,
    canPin: Boolean,
    onPostClick: (Long) -> Unit = {},
    onLikeClick: (Long, Boolean) -> Unit = { _, _ -> },
    onPinToggle: (Long, Boolean) -> Unit = { _, _ -> }
) {
    when {
        isLoading && posts.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                SpotSpinner()
            }
        }

        posts.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Spacer(modifier = Modifier.height(screenHeightDp(300.dp)))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.document),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(SpotTheme.colors.gray400),
                        modifier = Modifier.size(screenWidthDp(33.dp))
                    )
                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                    Text(
                        text = "아직 올라온 글이 없어요!",
                        style = SpotTheme.typography.medium_400,
                        color = SpotTheme.colors.gray400
                    )
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                posts.forEach { post ->
                    key(post.postId, post.isPinned) {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (canPin && value == SwipeToDismissBoxValue.EndToStart) {
                                    onPinToggle(post.postId, post.isPinned)
                                }
                                false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            enableDismissFromEndToStart = canPin,
                            backgroundContent = {
                                if (canPin) {
                                    Box(
                                        modifier = Modifier
                                            .padding(screenWidthDp(1.dp))
                                            .fillMaxSize()
                                            .clip(SpotShapes.Soft)
                                            .background(SpotTheme.colors.B400)
                                            .padding(horizontal = screenWidthDp(20.dp)),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Image(
                                            painter = painterResource(
                                                if (post.isPinned) R.drawable.ic_unpin else R.drawable.ic_pin
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.size(screenWidthDp(14.dp)),
                                            colorFilter = ColorFilter.tint(SpotTheme.colors.white)
                                        )
                                    }
                                }
                            },
                            content = {
                                PostListItem(
                                    item = post,
                                    onLikeClick = { onLikeClick(post.postId, post.isLiked) },
                                    onClick = { onPostClick(post.postId) }
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(screenHeightDp(5.dp)))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = SpotTheme.colors.gray200
                    )
                    Spacer(modifier = Modifier.height(screenHeightDp(5.dp)))
                }
            }
        }
    }
}
