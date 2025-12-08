package com.umcspot.spot.post.content

import ProfileImage
import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.umcspot.spot.designsystem.shapes.ShapeImageBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.korean
import com.umcspot.spot.post.model.postDetail.CommentResult
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.ui.state.UiState

@Composable
fun PostContentScreen(
    contentPadding: PaddingValues,
    postId: Long,
    postViewModel: PostViewModel = hiltViewModel(),
) {
    val uiState by postViewModel.uiState.collectAsStateWithLifecycle()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    // 🔥 postId가 바뀔 때마다 로딩
    LaunchedEffect(postId) {
        postViewModel.load(postId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad)
    ) {
        when (val state = uiState.data) {
            is UiState.Loading -> {
                Text(
                    text = "로딩 중...",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is UiState.Failure -> {
                Text(
                    text = "에러: ${state.msg}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is UiState.Empty -> {
                Text(
                    text = "데이터가 없습니다.",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is UiState.Success -> {
                PostContentDetailScreen(
                    modifier = Modifier.fillMaxSize(),
                    post = state.data       // 🔥 실제 PostDetailResult 전달
                )
            }
        }
    }
}


@Composable
fun PostContentDetailScreen(
    modifier : Modifier = Modifier,
    post: PostDetailResult,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        UserInfo(
            postWriterName = post.nickname,
            postWriterImage = post.profileImageUrl,
            postWriteAt = post.createdAt
        )

        PostDetailScreen(
            post.postType,
            post.title,
            post.imageUrl,
            post.content,
        )
    }
}

@Composable
fun UserInfo(
    modifier : Modifier = Modifier,
    postWriterName : String,
    postWriterImage : ImageRef,
    postWriteAt : String,
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
    ) {
        ProfileImage(
            imageRef = postWriterImage,
            modifier = Modifier.size(44.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column (

        ) {
            Text(
                text = postWriterName,
                style = SpotTheme.typography.medium_400
            )
            
            Spacer(Modifier.height(4.dp))

            Text(
                text = postWriteAt,
                style = SpotTheme.typography.regular_400
            )
        }
    }
}

@Composable
fun PostDetailScreen(
    postType: PostType,
    title: String,
    image: ImageRef,
    content: String
) {
    val context = LocalContext.current

    // 🔥 ImageRef → Painter
    val painter: Painter? = when (image) {
        ImageRef.None -> null

        is ImageRef.Name -> {
            val resId = remember(image.name) {
                context.resources.getIdentifier(
                    image.name,
                    "drawable",
                    context.packageName
                )
            }
            if (resId != 0) painterResource(id = resId) else null
        }

        is ImageRef.Url -> {
            rememberAsyncImagePainter(model = image.url)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "#${postType.korean}",      // 태그 느낌
            style = SpotTheme.typography.small_500,
            color = SpotTheme.colors.B500
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = title,
            style = SpotTheme.typography.h5,
            maxLines = Int.MAX_VALUE,
            softWrap = true,
        )

        // 🔥 여기 이미지 하나
        if (painter != null) {
            Spacer(Modifier.height(12.dp))
            ShapeImageBox(
                painter = painter,
                shape = SpotShapes.Hard,
                modifier = Modifier.wrapContentHeight(),
                borderWidth = 0.dp,
                padding = 0.dp,
                contentScale = ContentScale.FillWidth
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = content,
            style = SpotTheme.typography.medium_400
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPreview() {
    SpotTheme {
        PostContentDetailScreen(
            post = PostDetailResult.dummyPostDetail(123456, 5)
        )
    }
}


