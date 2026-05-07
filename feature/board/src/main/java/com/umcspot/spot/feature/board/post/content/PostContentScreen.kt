package com.umcspot.spot.feature.board.post.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.comment.CommentField
import com.umcspot.spot.designsystem.component.modal.AcceptDialog
import com.umcspot.spot.designsystem.component.modal.DeleteDialog
import com.umcspot.spot.designsystem.component.modal.ReportDialog
import com.umcspot.spot.designsystem.component.post.CommentUserInfo
import com.umcspot.spot.designsystem.component.post.CountView
import com.umcspot.spot.designsystem.component.post.UserInfo
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.korean
import com.umcspot.spot.post.model.postDetail.CommentResult
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.delay

@Composable
fun PostContentScreen(
    contentPadding: PaddingValues,
    postId: Long,
    onDeleteClick: () -> Unit,
    onEditClick:(Long) -> Unit,
    postViewModel: PostViewModel = hiltViewModel(),

) {
    val uiState by postViewModel.uiState.collectAsStateWithLifecycle()
    var showReportRequestDialog by remember { mutableStateOf(false) }
    var showDeleteRequestDialog by remember { mutableStateOf(false) }
    var showAcceptRequestDialog by remember { mutableStateOf(false) }

    var reason by rememberSaveable { mutableStateOf("") }

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    var commentText by rememberSaveable { mutableStateOf("") }

    val listState = rememberSaveable(postId, saver = LazyListState.Saver) {
        LazyListState()
    }

    var isCommentFocused by remember { mutableStateOf(false) }

    LaunchedEffect(postId) {
        postViewModel.load(postId)
        commentText = ""
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val noRipple = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad)
            .imePadding()
            .clickable(interactionSource = noRipple, indication = null) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
    ) {
        when (val state = uiState.data) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SpotSpinner(size = screenWidthDp(30.dp))
                }
            }

            is UiState.Failure -> {
                Text(
                    "에러: ${state.msg}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is UiState.Empty -> {
                Text("데이터가 없습니다.", color = Color.Gray, modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Success -> {
                val post = state.data
                val commentBarHeight = screenHeightDp(44.dp)

                LaunchedEffect(isCommentFocused, post.comments.size) {
                    if (isCommentFocused) {
                        val lastIndex = 2 + post.comments.size - 1
                        val target = lastIndex.coerceAtLeast(0)

                        delay(300)

                        listState.animateScrollToItem(target)
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = commentBarHeight + screenHeightDp(8.dp))
                ) {
                    item(key = "post_header") {
                        PostContentDetailScreen(
                            post = post,
                            onLikeClick = { postViewModel.toggleLike() },
                            onEditClick = {
                                onEditClick(post.postId)
                            },
                            onDeleteClick = {
                                showDeleteRequestDialog = true
                            },
                            onReportClick = {
                                showReportRequestDialog = true
                            }
                        )
                    }

                    item(key = "divider") {
                        Spacer(Modifier.height(screenHeightDp(8.dp)))
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = SpotTheme.colors.gray200
                        )
                        Spacer(Modifier.height(screenHeightDp(8.dp)))
                    }

                    items(
                        items = post.comments,
                        key = { it.commentId },
                    ) { comment ->
                        CommentItem(
                            comment = comment,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = screenWidthDp(17.dp))
                        )
                        Spacer(Modifier.height(screenHeightDp(13.dp)))
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = screenWidthDp(17.dp)),
                            thickness = 1.dp,
                            color = SpotTheme.colors.gray200
                        )
                        Spacer(Modifier.height(screenHeightDp(13.dp)))
                    }
                }

                CommentField(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = screenWidthDp(17.dp))
                        .padding(bottom = screenHeightDp(8.dp)),
                    canWrite = true,
                    comment = commentText,
                    onCommentChange = { commentText = it },
                    onSendComment = { text ->
                        postViewModel.sendComment(text)
                    },
                    onFocusChanged = { focused -> isCommentFocused = focused }
                )

                DeleteDialog(
                    visible = showDeleteRequestDialog,
                    modalTitle = "이 글을 삭제하시겠어요?",
                    modalDes = "한 번 삭제한 글은 되돌릴 수 없어요.",
                    okButtonText = "삭제",
                    onDismiss = {
                        showDeleteRequestDialog = false
                    },
                    onClick = {
                        showDeleteRequestDialog = false
                        postViewModel.deletePost()
                        onDeleteClick()
                    }
                )

                ReportDialog(
                    visible = showReportRequestDialog,
                    modalTitle = "게시글을 신고하시겠습니까?",
                    modalDes = "신고 이유를 작성해주세요.\nSPOT 내부 검토 후, 빠르게 처리합니다.",
                    reason = reason,
                    onReasonChange = { reason = it },
                    okButtonText = "완료",
                    onDismiss = { showReportRequestDialog = false },
                    onClick = { typed ->
                        showReportRequestDialog = false
                        showAcceptRequestDialog = true
                        postViewModel.reportPost(reason)
                    }
                )

                AcceptDialog(
                    visible = showAcceptRequestDialog,
                    modalTitle = "신고 완료",
                    modalDes = "게시글 신고가 완료되었어요.\n쾌적한 서비스 이용을 위해 항상 노력하겠습니다.",
                    okButtonText = "확인",
                    noButtonText = null,
                    onDismiss = { showAcceptRequestDialog = false },
                    onClick = { showAcceptRequestDialog = false }
                )
            }
        }
    }
}

@Composable
fun PostContentDetailScreen(
    modifier: Modifier = Modifier,
    post: PostDetailResult,
    onLikeClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(horizontal = screenWidthDp(17.dp))
            .padding(bottom = screenHeightDp(13.dp)),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UserInfo(
                postWriterName = post.nickname,
                postWriterImage = post.profileImageUrl,
                postWriteAt = post.createdAt
            )

            Box(
                modifier = Modifier
                    .size(screenWidthDp(33.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.meetball),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(screenWidthDp(14.dp))
                        .clickable { menuExpanded = true }
                )

                // 팝업 메뉴
                EditDeleteMenu(
                    isOwner = post.isOwner,
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    onEdit = { onEditClick() },
                    onDelete = { onDeleteClick() },
                    onReport = { onReportClick() }
                )
            }
        }

        Spacer(Modifier.height(screenHeightDp(12.dp)))

        PostDetailScreen(
            post.postType,
            post.title,
            post.imageUrl,
            post.content,
        )

        Spacer(Modifier.height(screenHeightDp(20.dp)))

        CountView(
            item = post,
            onLikeClick = { onLikeClick() }
        )
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

    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = "# ${postType.korean}",
            style = SpotTheme.typography.small_500,
            color = SpotTheme.colors.B500
        )

        Spacer(Modifier.height(screenHeightDp(4.dp)))

        Text(
            text = title,
            style = SpotTheme.typography.h5,
            maxLines = Int.MAX_VALUE,
            softWrap = true
        )
        when (image) {
            ImageRef.None -> Unit

            is ImageRef.Url -> {
                Spacer(Modifier.height(screenHeightDp(20.dp)))
                AsyncImage(
                    model = Builder(context)
                        .data(image.url)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(SpotShapes.Hard)
                )
            }

            is ImageRef.Name -> {
                val resId = remember(image.name) {
                    context.resources.getIdentifier(
                        image.name,
                        "drawable",
                        context.packageName
                    )
                }
                if (resId != 0) {
                    Spacer(Modifier.height(screenHeightDp(20.dp)))
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(SpotShapes.Hard)
                    )
                }
            }

            is ImageRef.LocalUri -> {
                Spacer(Modifier.height(screenHeightDp(20.dp)))
                AsyncImage(
                    model = Builder(context)
                        .data(image.uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(SpotShapes.Hard)
                )
            }
        }

        Spacer(Modifier.height(screenHeightDp(20.dp)))

        Text(
            text = content,
            style = SpotTheme.typography.medium_400,
            maxLines = Int.MAX_VALUE,
            softWrap = true,
        )
    }
}

@Composable
private fun CommentItem(
    comment: CommentResult,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        CommentUserInfo(
            commentWriterName = comment.nickname,
            commentWriterImage = comment.profileImageUrl
        )

        Spacer(Modifier.height(screenHeightDp(7.dp)))

        Text(
            text = comment.content.toString(),
            style = SpotTheme.typography.medium_400,
            color = SpotTheme.colors.black,
            maxLines = Int.MAX_VALUE,
            softWrap = true
        )
    }
}

@Composable
fun EditDeleteMenu(
    isOwner: Boolean,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier
            .background(SpotTheme.colors.white),
        shape = SpotShapes.Soft,
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        if (isOwner) {
            DropdownMenuItem(
                modifier = Modifier
                    .height(screenHeightDp(30.dp))
                    .wrapContentWidth(),
                text = {
                    Text(
                        text = "편집하기",
                        style = SpotTheme.typography.regular_500,
                        color = SpotTheme.colors.black
                    )
                },
                onClick = {
                    onDismiss()
                    onEdit()
                }
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = SpotTheme.colors.gray200
            )

            DropdownMenuItem(
                modifier = Modifier
                    .height(screenHeightDp(30.dp))
                    .wrapContentWidth(),
                text = {
                    Text(
                        text = "삭제하기",
                        style = SpotTheme.typography.regular_500,
                        color = SpotTheme.colors.R500
                    )
                },
                onClick = {
                    onDismiss()
                    onDelete()
                }
            )
        } else {
            DropdownMenuItem(
                modifier = Modifier
                    .height(screenHeightDp(30.dp))
                    .wrapContentWidth(),
                text = {
                    Text(
                        text = "신고하기",
                        style = SpotTheme.typography.regular_500,
                        color = SpotTheme.colors.R500
                    )
                },
                onClick = {
                    onDismiss()
                    onReport()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun preview() {
    val listState = rememberLazyListState()
    val dummyComments = List(5) { idx -> CommentResult.dummyComment(idx, 10) }

    SpotTheme {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
        ) {
            // 1) 상세 섹션
            item(key = "post_header") {
                PostContentDetailScreen(
                    post = PostDetailResult.dummyPostDetail(123456, 5),
                    onLikeClick = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onReportClick = {}
                )
            }

            // 2) 구분선
            item(key = "divider") {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = SpotTheme.colors.gray200
                )
                Spacer(Modifier.height(screenHeightDp(18.dp)))
            }

            items(
                items = dummyComments,
                key = { it.commentId }
            ) { comment ->
                CommentItem(
                    comment = comment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp)
                )
                Spacer(Modifier.height(13.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                    thickness = 1.dp,
                    color = SpotTheme.colors.gray200
                )
                Spacer(Modifier.height(13.dp))

            }
        }
    }
}


