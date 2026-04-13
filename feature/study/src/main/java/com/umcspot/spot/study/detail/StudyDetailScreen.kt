package com.umcspot.spot.study.detail

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.component.SpotStudyApplyDialog
import com.umcspot.spot.study.component.SpotStudyDialog
import com.umcspot.spot.study.detail.component.common.StudyDetailTabRow
import com.umcspot.spot.study.detail.component.common.StudyHeaderSection
import com.umcspot.spot.study.detail.component.planner.ScheduleBottomSheet
import com.umcspot.spot.study.detail.component.planner.ScheduleDetailBottomSheet
import com.umcspot.spot.study.detail.model.StudyDetailSideEffect
import com.umcspot.spot.study.detail.model.StudyDetailState
import com.umcspot.spot.study.detail.model.StudyDetailTab
import com.umcspot.spot.study.detail.screen.StudyDetailBoardScreen
import com.umcspot.spot.study.detail.screen.StudyDetailHomeScreen
import com.umcspot.spot.study.detail.screen.StudyDetailMemoirScreen
import com.umcspot.spot.study.detail.screen.StudyDetailPlannerScreen
import com.umcspot.spot.study.detail.screen.camera.QrScannerScreen
import com.umcspot.spot.study.model.ViewerStatus
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun StudyDetailRoute(
    studyId: Long,
    onBackClick: () -> Unit,
    onAttendanceClick: (Long) -> Unit,
    contentPadding: PaddingValues,
    onTabChanged: (StudyDetailTab) -> Unit,
    initialTab: StudyDetailTab,
    viewModel: StudyDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableStateOf(initialTab) }
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val isOwner = uiState.homeState.viewerStatus == ViewerStatus.OWNER
    val isMember = uiState.homeState.viewerStatus == ViewerStatus.APPROVED || isOwner

    var showScheduleBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showScheduleDetailBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedScheduleId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedScheduleIsNow by rememberSaveable { mutableStateOf(false) }

    var isScannerOpen by remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) isScannerOpen = true
    }

    var showApplyInputDialog by remember { mutableStateOf(false) }
    var showApplySuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is StudyDetailSideEffect.ApplySuccess -> {
                    showApplyInputDialog = false
                    showApplySuccessDialog = true
                }
                is StudyDetailSideEffect.ScheduleCreateSuccess -> {
                    showScheduleBottomSheet = false
                    viewModel.clearScheduleError()
                }
                else -> Unit
            }
        }
    }

    LaunchedEffect(studyId) {
        viewModel.fetchStudyHomeDetail(studyId)
    }

    LaunchedEffect(selectedTab) {
        onTabChanged(selectedTab)
        when (selectedTab) {
            StudyDetailTab.MEMOIR -> viewModel.fetchAllMemoirs(studyId)
            StudyDetailTab.PLANNER -> {
                val date = uiState.plannerState.selectedDate
                viewModel.fetchMonthlySchedules(studyId, date.year, date.monthValue)
            }
            else -> Unit
        }
    }

    DisposableEffect(Unit) {
        onDispose { onTabChanged(StudyDetailTab.HOME) }
    }

    BackHandler { onBackClick() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
    ) {
        StudyDetailScreen(
            studyId = studyId,
            uiState = uiState,
            selectedTab = selectedTab,
            isOwner = isOwner,
            isMember = isMember,
            onLikeClick = { viewModel.toggleLike(studyId) },
            onAttendanceClick = { scheduleId, isNow ->
                if (!isMember) return@StudyDetailScreen
                if (isOwner) {
                    onAttendanceClick(scheduleId)
                } else {
                    selectedScheduleId = scheduleId
                    selectedScheduleIsNow = isNow
                    showScheduleDetailBottomSheet = true
                }
            },
            onTabSelected = { selectedTab = it },
            onDateSelected = viewModel::updateSelectedDate,
            onMonthChanged = { y, m -> viewModel.fetchMonthlySchedules(studyId, y, m) },
            onAddingSchedule = { if (isOwner) showScheduleBottomSheet = true },
            onScheduleDelete = viewModel::deleteSchedule,
            onScheduleMenuToggle = viewModel::toggleScheduleMenu,
            onAddingTodo = {
                if (isMember) {
                    scope.launch {
                        delay(300)
                        lazyListState.animateScrollToItem(
                            lazyListState.layoutInfo.totalItemsCount - 1
                        )
                    }
                }
            },
            onTodoCreate = viewModel::createTodo,
            onTodoToggle = viewModel::toggleTodoStatus,
            onTodoDelete = viewModel::deleteTodo,
            onMemberSelected = { id ->
                viewModel.fetchMemberTodos(studyId, id, uiState.plannerState.selectedDate)
            },
            onMemoirDelete = { id -> viewModel.deleteMemoir(studyId, id) },
            onMemoirEmojiToggle = { id, type -> viewModel.toggleMemoirReaction(studyId, id, type) },
            onApplyClick = { showApplyInputDialog = true },
            onBackClick = onBackClick,
            contentPadding = contentPadding,
            lazyListState = lazyListState
        )

        if (!uiState.isLoading) {
            val status = uiState.homeState.viewerStatus
            if (status == ViewerStatus.NOT_APPLIED || status == ViewerStatus.APPLIED) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(SpotTheme.colors.white)
                        .padding(horizontal = screenWidthDp(17.dp))
                        .padding(bottom = contentPadding.calculateBottomPadding() + 12.dp)
                        .imePadding()
                ) {
                    SpotActivationButton(
                        modifier = Modifier.fillMaxWidth(),
                        buttonText = if (status == ViewerStatus.APPLIED) "승인 대기 중" else "신청하기",
                        isEnabled = status == ViewerStatus.NOT_APPLIED,
                        onClick = {
                            if (status == ViewerStatus.NOT_APPLIED) showApplyInputDialog = true
                        }
                    )
                }
            }
        }

        if (isScannerOpen) {
            QrScannerScreen(
                onQrScanned = { isScannerOpen = false },
                onClose = { isScannerOpen = false }
            )
        }

        ScheduleDetailBottomSheet(
            visible = showScheduleDetailBottomSheet,
            onDismiss = { showScheduleDetailBottomSheet = false },
            isNow = selectedScheduleIsNow,
            onAttendanceClick = {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
            onDeleteClick = {
                selectedScheduleId?.let { id ->
                    viewModel.deleteSchedule(studyId, id)
                }
                showScheduleDetailBottomSheet = false
            }
        )

        ScheduleBottomSheet(
            visible = showScheduleBottomSheet,
            onDismiss = {
                showScheduleBottomSheet = false
                viewModel.clearScheduleError()
            },
            studyId = studyId,
            isOverlapError = uiState.plannerState.isOverlapError,
            onClearError = { viewModel.clearScheduleError() },
            onCreateSchedule = { title, memo, start, end ->
                viewModel.createSchedule(studyId, title, memo, start, end)
            }
        )

        if (showApplyInputDialog) {
            SpotStudyApplyDialog(
                onDismissRequest = { showApplyInputDialog = false },
                onApplySubmit = { message -> viewModel.applyStudy(studyId, message) }
            )
        }

        if (showApplySuccessDialog) {
            SpotStudyDialog(
                onDismissRequest = { showApplySuccessDialog = false },
                title = "신청 완료",
                description = "스터디를 신청 완료했어요!\n수락 여부는 알람 탭에서 확인 가능해요.",
                buttonText = "확인",
                showCheckIcon = true,
                onButtonClick = { showApplySuccessDialog = false }
            )
        }
    }
}

@Composable
private fun StudyDetailScreen(
    studyId: Long,
    uiState: StudyDetailState,
    selectedTab: StudyDetailTab,
    isOwner: Boolean,
    isMember: Boolean,
    onLikeClick: () -> Unit,
    onAttendanceClick: (Long, Boolean) -> Unit,
    onTabSelected: (StudyDetailTab) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (Int, Int) -> Unit,
    onAddingSchedule: () -> Unit,
    onScheduleDelete: (Long, Long) -> Unit,
    onScheduleMenuToggle: (Long) -> Unit,
    onAddingTodo: () -> Unit,
    onTodoCreate: (Long, String) -> Unit,
    onTodoToggle: (Long, Long, Boolean) -> Unit,
    onTodoDelete: (Long, Long) -> Unit,
    onMemberSelected: (Long) -> Unit,
    onMemoirDelete: (Long) -> Unit,
    onMemoirEmojiToggle: (Long, String) -> Unit,
    onApplyClick: () -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
    lazyListState: LazyListState
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        item {
            Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))
            BackTopBar(title = "스터디", onBackClick = onBackClick)
            AsyncImage(
                model = uiState.homeState.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeightDp(160.dp)),
                contentScale = if (uiState.homeState.thumbnailUrl != null) ContentScale.Crop else ContentScale.Fit,
                placeholder = painterResource(R.drawable.ic_default),
                error = painterResource(R.drawable.ic_default)
            )
            StudyHeaderSection(
                homeState = uiState.homeState,
                onLikeClick = onLikeClick
            )
        }

        item {
            StudyDetailTabRow(selectedTab = selectedTab, onTabSelected = onTabSelected)
            Spacer(modifier = Modifier.height(screenHeightDp(18.dp)))
        }

        item {
            val bottomPadding = if (
                uiState.homeState.viewerStatus == ViewerStatus.NOT_APPLIED ||
                uiState.homeState.viewerStatus == ViewerStatus.APPLIED
            ) 100.dp else 20.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(17.dp))
                    .padding(bottom = contentPadding.calculateBottomPadding() + bottomPadding)
            ) {
                when (selectedTab) {
                    StudyDetailTab.HOME -> StudyDetailHomeScreen(
                        description = uiState.homeState.studyDescription,
                        members = uiState.homeState.members,
                        schedules = uiState.homeState.schedules,
                        recentMemoirs = uiState.homeState.recentMemoirs,
                        isMember = isMember,
                        onAttendanceClick = { id, isNow -> onAttendanceClick(id, isNow) }
                    )
                    StudyDetailTab.PLANNER -> StudyDetailPlannerScreen(
                        studyId = studyId,
                        plannerState = uiState.plannerState,
                        members = uiState.homeState.members,
                        isOwner = isOwner,
                        isMember = isMember,
                        onAttendanceClick = { id, isNow -> onAttendanceClick(id, isNow) },
                        onDateSelected = onDateSelected,
                        onMonthChanged = onMonthChanged,
                        onAddingSchedule = onAddingSchedule,
                        onAddingTodo = onAddingTodo,
                        onTodoCreate = onTodoCreate,
                        onTodoToggle = onTodoToggle,
                        onTodoDelete = onTodoDelete,
                        onMemberSelected = onMemberSelected
                    )
                    StudyDetailTab.BOARD -> StudyDetailBoardScreen()
                    StudyDetailTab.MEMOIR -> StudyDetailMemoirScreen(
                        studyId = studyId,
                        memoirs = uiState.memoirState.memoirs,
                        onDeleteMemoir = onMemoirDelete,
                        onEmojiToggle = onMemoirEmojiToggle
                    )
                }
            }
        }
    }
}