package com.umcspot.spot.study.detail.screen

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.StudyDetailViewModel
import com.umcspot.spot.study.model.CommentResult
import com.umcspot.spot.study.model.StudyPostDetailResult
import com.umcspot.spot.study.model.StudyPostResult
import com.umcspot.spot.study.model.ViewerStatus
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.delay

@Composable
fun StudyPostContentScreen(
    contentPadding: PaddingValues,
    studyId: Long,
    postId: Long,
    onDeleteClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onSendComment: (Long, String) -> Unit = { _, _ -> },
    onDeletePost: (Long, Long) -> Unit = { _, _ -> },
    onReportPost: (Long, Long, String) -> Unit = { _, _, _ -> },
    viewModel: StudyDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showReportRequestDialog by remember { mutableStateOf(false) }
    var showDeleteRequestDialog by remember { mutableStateOf(false) }
    var showAcceptRequestDialog by remember { mutableStateOf(false) }

    var reason by rememberSaveable { mutableStateOf("") }
    var commentText by rememberSaveable { mutableStateOf("") }
    var isCommentFocused by remember { mutableStateOf(false) }

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val listState = rememberSaveable(postId, saver = LazyListState.Saver) {
        LazyListState()
    }

    LaunchedEffect(studyId, postId) {
        viewModel.fetchStudyPostDetail(studyId, postId)
        commentText = ""
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.clearStudyPostDetail() }
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
        when (val state = uiState.postDetailState) {
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
                val post = state.data
                val canWriteComment = uiState.homeState.viewerStatus == ViewerStatus.APPROVED ||
                    uiState.homeState.viewerStatus == ViewerStatus.OWNER ||
                    post.isOwner
                val commentBarHeight = if (canWriteComment) screenHeightDp(44.dp) else 0.dp

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
                            isOwner = post.isOwner,
                            onLikeClick = { viewModel.toggleStudyPostDetailLike() },
                            onEditClick = { onEditClick(post.postId) },
                            onDeleteClick = { showDeleteRequestDialog = true },
                            onReportClick = { showReportRequestDialog = true }
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
                        key = { it.commentId }
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

                if (canWriteComment) {
                    CommentField(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = screenWidthDp(17.dp))
                            .padding(bottom = screenHeightDp(8.dp)),
                        canWrite = true,
                        comment = commentText,
                        onCommentChange = { commentText = it },
                        onSendComment = { text ->
                            val trimmed = text.trim()
                            if (trimmed.isNotEmpty()) {
                                viewModel.sendStudyPostComment(studyId, post.postId, trimmed)
                                onSendComment(post.postId, trimmed)
                                commentText = ""
                            }
                        },
                        onFocusChanged = { focused -> isCommentFocused = focused }
                    )
                }

                DeleteDialog(
                    visible = showDeleteRequestDialog,
                    modalTitle = "게시글을 삭제하시겠어요?",
                    modalDes = "한 번 삭제한 게시글은 되돌릴 수 없어요.",
                    okButtonText = "삭제",
                    onDismiss = { showDeleteRequestDialog = false },
                    onClick = {
                        showDeleteRequestDialog = false
                        onDeletePost(studyId, post.postId)
                        onDeleteClick()
                    }
                )

                ReportDialog(
                    visible = showReportRequestDialog,
                    modalTitle = "게시글을 신고하시겠습니까?",
                    modalDes = "신고 사유를 작성해주세요.\nSPOT 팀이 검토 후 빠르게 처리합니다.",
                    reason = reason,
                    onReasonChange = { reason = it },
                    okButtonText = "완료",
                    onDismiss = { showReportRequestDialog = false },
                    onClick = { typedReason ->
                        showReportRequestDialog = false
                        showAcceptRequestDialog = true
                        onReportPost(studyId, post.postId, typedReason)
                    }
                )

                AcceptDialog(
                    visible = showAcceptRequestDialog,
                    modalTitle = "신고 완료",
                    modalDes = "게시글 신고가 완료되었습니다.\n쾌적한 서비스 이용을 위해 항상 노력하겠습니다.",
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
    post: StudyPostDetailResult,
    isOwner: Boolean,
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UserInfo(
                postWriterName = post.writerNickname,
                postWriterImage = post.writerProfileUrl,
                postWriteAt = post.createdAt
            )

            Box(modifier = Modifier.size(screenWidthDp(33.dp))) {
                Image(
                    painter = painterResource(R.drawable.meetball),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(screenWidthDp(14.dp))
                        .clickable { menuExpanded = true }
                )

                EditDeleteMenu(
                    isOwner = isOwner,
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    onEdit = onEditClick,
                    onDelete = onDeleteClick,
                    onReport = onReportClick
                )
            }
        }

        Spacer(Modifier.height(screenHeightDp(12.dp)))

        PostDetailScreen(
            title = post.title,
            content = post.content
        )

        Spacer(Modifier.height(screenHeightDp(20.dp)))

        CountView(
            item = StudyPostResult(
                postId = post.postId,
                title = post.title,
                content = post.content,
                isPinned = post.isPinned,
                isLiked = post.isLiked,
                likeCount = post.likeCount,
                viewCount = post.viewCount,
                commentCount = post.commentCount,
                createdAt = post.createdAt
            ),
            onLikeClick = { onLikeClick() }
        )
    }
}

@Composable
fun PostDetailScreen(
    title: String,
    content: String
) {
    Column(modifier = Modifier.wrapContentSize()) {
        Text(
            text = title,
            style = SpotTheme.typography.h5,
            maxLines = Int.MAX_VALUE,
            softWrap = true
        )

        Spacer(Modifier.height(screenHeightDp(20.dp)))

        Text(
            text = content,
            style = SpotTheme.typography.medium_400,
            maxLines = Int.MAX_VALUE,
            softWrap = true
        )
    }
}

@Composable
private fun CommentItem(
    comment: CommentResult,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CommentUserInfo(
            commentWriterName = comment.commentNickname,
            commentWriterImage = comment.commentProfileUrl
        )

        Spacer(Modifier.height(screenHeightDp(7.dp)))

        Text(
            text = comment.content,
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
        modifier = Modifier.background(SpotTheme.colors.white),
        shape = SpotShapes.Soft,
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        if (isOwner) {
            DropdownMenuItem(
                modifier = Modifier
                    .height(screenHeightDp(30.dp))
                    .wrapContentWidth(),
                text = {
                    Text(
                        text = "수정하기",
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
