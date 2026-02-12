package com.umcspot.spot.study.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.component.common.StudyDetailTabRow
import com.umcspot.spot.study.detail.component.common.StudyHeaderSection
import com.umcspot.spot.study.detail.model.StudyDetailState
import com.umcspot.spot.study.detail.model.StudyDetailTab
import com.umcspot.spot.study.detail.screen.StudyDetailBoardScreen
import com.umcspot.spot.study.detail.screen.StudyDetailHomeScreen
import com.umcspot.spot.study.detail.screen.StudyDetailMemoirScreen
import com.umcspot.spot.study.detail.screen.StudyDetailPlannerScreen
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun StudyDetailRoute(
    studyId: Long,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
    onTabChanged: (StudyDetailTab) -> Unit,
    initialTab: StudyDetailTab,
    viewModel: StudyDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableStateOf(initialTab) }
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(studyId) {
        viewModel.fetchStudyHomeDetail(studyId)
        val memberId = uiState.plannerState.selectedMemberId.toLongOrNull()
        if (memberId != null) {
            viewModel.fetchMemberTodos(studyId, memberId, LocalDate.now())
        }
    }

    LaunchedEffect(selectedTab) {
        onTabChanged(selectedTab)
    }

    DisposableEffect(Unit) {
        onDispose { onTabChanged(StudyDetailTab.HOME) }
    }

    BackHandler { onBackClick() }

    StudyDetailScreen(
        studyId = studyId,
        uiState = uiState,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        onDateSelected = viewModel::updateSelectedDate,
        onMonthChanged = { year, month -> viewModel.fetchMonthlySchedules(studyId, year, month) },
        onAddingTodo = {
            scope.launch {
                delay(300)
                lazyListState.animateScrollToItem(lazyListState.layoutInfo.totalItemsCount - 1, -300)
            }
        },
        onTodoCreate = viewModel::createTodo,
        onTodoToggle = viewModel::toggleTodoStatus,
        onTodoDelete = viewModel::deleteTodo,
        onMemberSelected = { memberId ->
            viewModel.fetchMemberTodos(
                studyId = studyId,
                memberId = memberId,
                date = uiState.plannerState.selectedDate
            )
        },
        onMemoirDelete = { memoirId -> viewModel.deleteMemoir(studyId, memoirId) },
        onMemoirEmojiToggle = viewModel::toggleMemoirReaction,
        onBackClick = onBackClick,
        contentPadding = contentPadding,
        lazyListState = lazyListState
    )
}

@Composable
private fun StudyDetailScreen(
    studyId: Long,
    uiState: StudyDetailState,
    selectedTab: StudyDetailTab,
    onTabSelected: (StudyDetailTab) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (Int, Int) -> Unit,
    onAddingTodo: () -> Unit,
    onTodoCreate: (Long, String) -> Unit, 
    onTodoToggle: (Long, Long, Boolean) -> Unit,
    onTodoDelete: (Long, Long) -> Unit, 
    onMemberSelected: (Long) -> Unit,   
    onMemoirDelete: (Long) -> Unit,
    onMemoirEmojiToggle: (Long, Long, String, Boolean) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
    lazyListState: LazyListState
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
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
                contentScale = ContentScale.Crop
            )

            StudyHeaderSection(uiState.homeState)
        }

        item {
            StudyDetailTabRow(selectedTab = selectedTab, onTabSelected = onTabSelected)
            Spacer(modifier = Modifier.height(screenHeightDp(18.dp)))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(17.dp)) 
                    .padding(bottom = contentPadding.calculateBottomPadding() + screenHeightDp(20.dp))
            ) {
                when (selectedTab) {
                    StudyDetailTab.HOME -> StudyDetailHomeScreen(
                        description = uiState.homeState.studyDescription,
                        members = uiState.homeState.members,
                        schedules = uiState.homeState.schedules,
                        recentMemoirs = uiState.homeState.recentMemoirs
                    )

                    StudyDetailTab.PLANNER -> StudyDetailPlannerScreen(
                        studyId = studyId,
                        plannerState = uiState.plannerState,
                        members = uiState.homeState.members,
                        onDateSelected = onDateSelected,
                        onMonthChanged = onMonthChanged,
                        onAddingTodo = onAddingTodo,
                        onTodoCreate = onTodoCreate, 
                        onTodoToggle = onTodoToggle,
                        onTodoDelete = onTodoDelete,
                        onMemberSelected = onMemberSelected
                    )

                    StudyDetailTab.BOARD -> StudyDetailBoardScreen(
                        studyId = studyId
                    )
                    StudyDetailTab.MEMOIR -> StudyDetailMemoirScreen(
                        studyId = studyId,
                        memoirs = uiState.memoirState.memoirs,
                        onDeleteMemoir = onMemoirDelete,
                        onEmojiToggle = { memoirId, type, isSelected ->
                            onMemoirEmojiToggle(studyId, memoirId, type, isSelected)
                        }
                    )
                }
            }
        }
    }
}