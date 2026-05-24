package com.umcspot.spot.mypage.recruiting.application

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.ProfileImage
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.component.empty.EmptyAlert
import com.umcspot.spot.designsystem.component.modal.AcceptDialog
import com.umcspot.spot.designsystem.component.modal.RejectDialog
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyApplicationResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun RecruitingStudyRequestScreen(
    contentPadding : PaddingValues,
    studyId : Long,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    viewmodel : RecruitingStudyApplicationViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    var showRejectDialog by rememberSaveable { mutableStateOf(false) }
    var showAcceptDialog by rememberSaveable { mutableStateOf(false) }

    var selectedApplicationId by rememberSaveable { mutableStateOf<Long?>(null) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val ui = uiState.applications
    val itemList: List<StudyApplicationResult> = when (ui) {
        is UiState.Success -> ui.data.applies
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        viewmodel.load(studyId)
        onRegisterScrollToTop {
            scope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    when (ui) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                SpotSpinner(size = screenWidthDp(30.dp))
            }
        }
        is UiState.Empty, is UiState.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                EmptyAlert(
                    alertTitle = "신청자가 아직 없어요!",
                    alertDes = "SPOT과 함께 새로운 만남을 기다려봐요",
                    painter = painterResource(R.drawable.group_add),
                )
            }
        }
        is UiState.Success -> {
            RecruitingStudyScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white)
                    .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding())
                    .padding(horizontal = screenWidthDp(17.dp)),
                applicationList = itemList,
                listState = listState,
                onAcceptClick = {
                    viewmodel.accept(it)
                    showAcceptDialog = true
                },
                onRejectClick = {
                    selectedApplicationId = it
                    showRejectDialog = true
                },
            )

            RejectDialog(
                visible = showRejectDialog,
                painter = painterResource(R.drawable.group_refuse),
                painterTint = SpotTheme.colors.R500,
                modalTitle = "거절하시겠어요?",
                modalDes = "신청자에게 거절 알림이 전송되지 않아요.",
                okButtonText = "거절",
                noButtonText = "취소",
                onDismiss = {
                    showRejectDialog = false
                },
                onClick = {
                    showRejectDialog = false
                    selectedApplicationId?.let { viewmodel.reject(it) }
                },
                onCancel = {
                    showRejectDialog = false
               },
            )

            AcceptDialog(
                visible = showAcceptDialog,
                painter = painterResource(R.drawable.group_accept),
                painterTint = SpotTheme.colors.B500,
                modalTitle = "수락 완료",
                modalDes = "수락이 완료되었어요.",
                okButtonText = "확인",
                noButtonText = null,
                onDismiss = {
                    showAcceptDialog = false
                },
                onClick = {
                    showAcceptDialog = false
                }
            )
        }
    }
}

@Composable
fun RecruitingStudyScreenContent(
    modifier: Modifier = Modifier,
    applicationList: List<StudyApplicationResult>,
    listState: LazyListState,
    onAcceptClick: (Long) -> Unit,
    onRejectClick: (Long) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        items(
            items = applicationList,
            key = { it.applicantId }
        ) { item ->
            Spacer(Modifier.height(screenHeightDp(5.dp)))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                ApplicationItem(
                    applicationInfo = item,
                    onAcceptClick = onAcceptClick,
                    onRejectClick = onRejectClick
                )
            }

            if (applicationList.indexOf(item) != applicationList.lastIndex) {
                Spacer(Modifier.height(screenHeightDp(5.dp)))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun ApplicationItem(
    applicationInfo : StudyApplicationResult,
    onAcceptClick: (Long) -> Unit,
    onRejectClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier.padding((screenWidthDp(7.dp)))
    ) {
        Row {
            ProfileImage(
                imageRef = applicationInfo.profileImageUrl,
                modifier = Modifier.size(55.dp)
            )

            Spacer(modifier = Modifier.width(screenWidthDp(13.dp)))

            Column {
                Text(
                    text = applicationInfo.nickname,
                    style = SpotTheme.typography.h5
                )
                Text(
                    text = applicationInfo.description,
                    style = SpotTheme.typography.medium_400,
                    color = SpotTheme.colors.G500
                )
            }
        }

        Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            TextButton(
                modifier = Modifier
                    .width(screenWidthDp(120.dp))
                    .height(screenHeightDp(26.dp)),
                text = "거절",
                style = SpotTheme.typography.regular_500,
                onClick = { onRejectClick(applicationInfo.applicantId) },
                state = TextButtonState.R500State
            )

            Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))

            TextButton(
                modifier = Modifier.width(screenWidthDp(120.dp)).height(screenHeightDp(26.dp)),
                text = "수락",
                style = SpotTheme.typography.regular_500,
                onClick = { onAcceptClick(applicationInfo.applicantId) },
                state = TextButtonState.B500State
            )
        }
    }
}