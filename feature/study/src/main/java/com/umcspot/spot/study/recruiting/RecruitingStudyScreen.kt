package com.umcspot.spot.study.recruiting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.empty.EmptyAlert
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.study.recruiting.RecruitingStudyViewModel
import timber.log.Timber

@Composable
fun RecruitingStudyScreen(
    contentPadding : PaddingValues,
    viewmodel: RecruitingStudyViewModel = hiltViewModel(),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onFilterClick : () -> Unit,
    onItemClick : (StudyResult) -> Unit
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val sort by viewmodel.sortType.collectAsStateWithLifecycle()

    var showSortSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    // 최초 진입 시 한 번 로드
    LaunchedEffect(state.studies) {
        if (state.studies is UiState.Empty) {
            viewmodel.load(RecruitingStudySort.LATEST)
        }
    }

    LaunchedEffect(Unit) {
        onRegisterScrollToTop {
            scope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    when (val state = state.studies) {
        is UiState.Loading -> Text("로딩 중...", color = Color.Gray)

        is UiState.Failure -> Text("에러: ${state.msg}", color = Color.Red)

        UiState.Empty -> {
            Text(text = "데이터가 없습니다.")
        }

        is UiState.Success -> {
            RecruitingStudyScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white)
                    .padding(top = topPad, bottom = bottomPad, start = 16.dp, end = 16.dp),
                studies = state.data.studyList,
                sortType = sort,
                listState = listState,
                onOpenSortSheet = { showSortSheet = true },
                onFilterClick = onFilterClick,
                onItemClick = onItemClick,
            )
        }
    }

    // 정렬 바텀시트
    if (showSortSheet) {
        SortTypeBottomSheet(
            current = sort,
            onSelect = { viewmodel.selectSort(it) },
            onDismiss = { showSortSheet = false }
        )
    }
}

@Composable
private fun RecruitingStudyScreenContent(
    modifier: Modifier = Modifier,
    studies: List<StudyResult>,
    listState: LazyListState,
    sortType : RecruitingStudySort,
    onOpenSortSheet : () -> Unit,
    onFilterClick: () -> Unit,
    onItemClick: (StudyResult) -> Unit,
    ) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // 타이틀
        Text(
            text = "모집중 스터디",
            style = SpotTheme.typography.bodySmall400.copy(fontSize = 20.sp)
        )

        Spacer(Modifier.height(8.dp))

        HeaderRow(
            size = studies.size,
            sortType = sortType,
            onOpenSortSheet = onOpenSortSheet,
            onFilterClick = onFilterClick
        )

        if (studies.isEmpty()) {
            // 빈 상태
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyAlert(
                    painter = painterResource(R.drawable.alert),
                    alertTitle = "현재 모집중인 스터디가 없어요",
                    alertDes = "필터를 변경하거나 나중에 다시 확인해보세요."
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(
                    items = studies,
                    key = { it.studyId }
                ) { item ->
                    StudyListItem(
                        item = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderRow(
    size : Int,
    sortType : RecruitingStudySort,
    onOpenSortSheet: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "%02d건".format(size),
            style = SpotTheme.typography.bodyMedium500.copy(fontSize = 12.sp),
            color = SpotTheme.colors.gray500
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                onClick = onOpenSortSheet,
                shape = SpotShapes.Soft,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = sortType.label,
                    color = SpotTheme.colors.black,
                    style = SpotTheme.typography.bodyMedium500.copy(fontSize = 12.sp)
                )
                Spacer(Modifier.width(5.dp))
                Icon(
                    painter = painterResource(R.drawable.arrow_down),
                    tint = SpotTheme.colors.B500,
                    contentDescription = null
                )
            }

            IconButton(onClick = onFilterClick) {
                Icon(
                    painter = painterResource(R.drawable.filter),
                    contentDescription = "필터",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortTypeBottomSheet(
    current: RecruitingStudySort,
    onSelect: (RecruitingStudySort) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SpotTheme.colors.white,
        dragHandle = { },
        contentWindowInsets = { WindowInsets(0) },
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(vertical = 8.dp)
        ) {
            RecruitingStudySort.entries.forEachIndexed { index, option ->
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = SpotTheme.colors.white,
                        headlineColor = SpotTheme.colors.black,   // (선택) 텍스트 색 명시
                        trailingIconColor = SpotTheme.colors.B500 // (선택)
                    ),
                    headlineContent = {
                        Text(
                            text = option.label,
                            style = SpotTheme.typography.bodyMedium500.copy(fontSize = 15.sp)
                        )
                    },
                    trailingContent = {
                        if (option == current) {
                            Icon(
                                painter = painterResource(R.drawable.success_default),
                                tint = SpotTheme.colors.B500,
                                modifier = Modifier.size(20.dp),
                                contentDescription = "선택됨",
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            onSelect(option)
                            onDismiss()
                        }
                )
                if (index != RecruitingStudySort.entries.lastIndex) {
                    HorizontalDivider(
                        color = SpotTheme.colors.G300,
                        thickness = 0.6.dp
                    )
                }
            }
        }
    }
}