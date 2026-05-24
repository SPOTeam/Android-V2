package com.umcspot.spot.mypage.participating

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.dialog.DeleteStudyDialog
import com.umcspot.spot.designsystem.component.dialog.ReportMemberDialog
import com.umcspot.spot.designsystem.component.empty.EmptyAlertWithButton
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.study.component.SpotStudyDialog
import com.umcspot.spot.study.component.SpotStudyDialogIcon
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun ParticipatingRoute(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit, 
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onStudyClick: (Long) -> Unit,
    moveToRecruitingStudy: () -> Unit,
    navigateToEditStudy: (Long) -> Unit,
    navigateToLeaveStudy: (Long, Boolean, String, String, String?) -> Unit,
    viewmodel: ParticipatingStudyViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val ui = uiState.participatingStudy
    val itemList: List<StudyResult> = when (ui) {
        is UiState.Success -> ui.data.studyList
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        viewmodel.loadParticipatingStudy()
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleItemIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            val successData = (ui as? UiState.Success)?.data
            if (successData?.hasNext == true) viewmodel.loadNextPage()
        }
    }

    
    ReportMemberDialog(
        visible = uiState.isReportDialogVisible,
        members = uiState.studyMembers,
        isMemberLoading = uiState.isMemberLoading,
        selectedMemberId = uiState.selectedMemberId,
        isReportStep = uiState.isReportStep,
        reportReason = uiState.reportReason,
        onMemberSelect = viewmodel::onMemberSelect,
        onReasonChange = viewmodel::onReportReasonChange,
        onNext = viewmodel::goToReportReasonStep,
        onSubmit = { viewmodel.submitReport() },
        onDismiss = viewmodel::dismissReportDialog
    )

    if (uiState.isReportSuccess) {
        SpotStudyDialog(
            onDismissRequest = { viewmodel.dismissReportSuccess() },
            title = "스터디원 신고",
            description = "스터디원 신고가 완료됐어요!\n쾌적한 서비스 이용을 위해 항상 노력할게요.",
            buttonText = "확인",
            icon = SpotStudyDialogIcon.CHECK,
            onButtonClick = { viewmodel.dismissReportSuccess() }
        )
    }

    DeleteStudyDialog(
        visible = uiState.isDeleteDialogVisible,
        onDelete = { viewmodel.deleteStudy() },
        onDismiss = viewmodel::dismissDeleteDialog
    )

    if (uiState.isDeleteSuccess) {
        SpotStudyDialog(
            onDismissRequest = { viewmodel.dismissDeleteSuccess() },
            title = "스터디 삭제 완료",
            description = "",
            buttonText = "확인",
            icon = SpotStudyDialogIcon.CHECK,
            onButtonClick = { viewmodel.dismissDeleteSuccess() }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            BackTopBar(
                title = "참여 중인 스터디",
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )

            when (ui) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        SpotSpinner(size = screenWidthDp(30.dp))
                    }
                }

                is UiState.Empty, is UiState.Failure -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        EmptyAlertWithButton(
                            alertTitle = "참여 중인 스터디가 아직 없어요!",
                            alertDes = "스팟에서 내 목표를 이뤄봐요",
                            buttonText = "스터디 둘러보기",
                            painter = painterResource(R.drawable.study_default),
                            onClick = { moveToRecruitingStudy() },
                        )
                    }
                }

                is UiState.Success -> {
                    ParticipatingScreenContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = screenWidthDp(17.dp)),
                        studyList = itemList,
                        listState = listState,
                        onStudyClick = onStudyClick,
                        onEditClick = { studyId -> navigateToEditStudy(studyId) },
                        onReportClick = { studyId -> viewmodel.openReportDialog(studyId) },
                        onLeaveClick = { studyId ->
                            val item = itemList.find { it.id == studyId }
                            item?.let {
                                navigateToLeaveStudy(
                                    it.id, it.isOwner, it.name, it.description,
                                    when (val img = it.profileImageUrl) {
                                        is ImageRef.Url -> img.url
                                        else -> null
                                    }
                                )
                            }
                        },
                        onDeleteClick = { studyId -> viewmodel.openDeleteDialog(studyId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ParticipatingScreenContent(
    modifier: Modifier = Modifier,
    studyList: List<StudyResult>,
    listState: LazyListState,
    onStudyClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onReportClick: (Long) -> Unit,
    onLeaveClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    var expandedForId by remember { mutableStateOf<Long?>(null) }

    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        items(
            items = studyList,
            key = { it.id }
        ) { item ->
            Spacer(Modifier.height(screenHeightDp(5.dp)))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                
                StudyListItem(
                    item = item,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onStudyClick(item.id) },
                    meetballSlot = {
                        Box {
                            Box(
                                modifier = Modifier
                                    .padding(top = screenHeightDp(4.dp))
                                    .width(screenWidthDp(24.dp))
                                    .height(screenWidthDp(22.dp))
                                    .clip(SpotShapes.Hard)
                                    .clickable { expandedForId = item.id },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.meetball),
                                    contentDescription = null,
                                    modifier = Modifier.size(screenWidthDp(14.dp))
                                )
                            }
                            MeetballMenu(
                                isOwner = item.isOwner,
                                isAlone = item.isAlone,
                                expanded = expandedForId == item.id,
                                onDismiss = { expandedForId = null },
                                onEdit = { expandedForId = null; onEditClick(item.id) },
                                onReport = { expandedForId = null; onReportClick(item.id) },
                                onLeave = { expandedForId = null; onLeaveClick(item.id) },
                                onDelete = { expandedForId = null; onDeleteClick(item.id) }
                            )
                        }
                    }
                )

                if (item.isOwner) {
                    Box(
                        modifier = Modifier
                            .padding(start = screenWidthDp(7.dp), top = screenWidthDp(7.dp))
                            .size(screenWidthDp(73.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 5.dp, y = 5.dp) 
                                .size(screenWidthDp(18.dp))
                                .background(Color.White, CircleShape)
                                .padding(screenWidthDp(2.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_leader),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            if (studyList.indexOf(item) != studyList.lastIndex) {
                Spacer(Modifier.height(screenHeightDp(5.dp)))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun MeetballMenu(
    isOwner: Boolean,
    isAlone: Boolean,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onReport: () -> Unit,
    onLeave: () -> Unit,
    onDelete: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier.background(SpotTheme.colors.white),
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
                        text = "정보 수정하기",
                        style = SpotTheme.typography.regular_500,
                        color = SpotTheme.colors.black
                    )
                },
                onClick = {
                    onDismiss()
                    onEdit()
                }
            )
        }

        if (!isAlone) {
            if (isOwner) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = SpotTheme.colors.gray200
                )
            }

            DropdownMenuItem(
                modifier = Modifier
                    .height(screenHeightDp(30.dp))
                    .wrapContentWidth(),
                text = {
                    Text(
                        text = "스터디원 신고",
                        style = SpotTheme.typography.regular_500,
                        color = SpotTheme.colors.black
                    )
                },
                onClick = {
                    onDismiss()
                    onReport()
                }
            )
        }

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
                    text = if (isAlone) "스터디 삭제하기" else "스터디 나가기",
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.R500
                )
            },
            onClick = {
                onDismiss()
                if (isAlone) onDelete()
                else onLeave()
            }
        )
    }
}