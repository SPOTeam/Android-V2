package com.umcspot.spot.study.preferLocation

import PreferLocationBottomSheet
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.zIndex
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
import com.umcspot.spot.designsystem.component.empty.EmptyAlertWithButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.study.recruiting.RecruitingStudyViewModel

@Composable
fun PreferLocationStudyScreen(
    contentPadding : PaddingValues,
    viewmodel: PreferLocationStudyViewModel = hiltViewModel(),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onFilterClick : () -> Unit,
    onItemClick : (StudyResult) -> Unit
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val sort by viewmodel.sortType.collectAsStateWithLifecycle()

    val query by viewmodel.query.collectAsStateWithLifecycle()
    val results by viewmodel.results.collectAsStateWithLifecycle()

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
        viewmodel.loadLocationData() // ✅ 최초 1회 자산 로드
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

        is UiState.Empty -> {
            Text(text = "데이터가 없습니다.")
        }

        is UiState.Success -> {
            PreferLocationStudyScreenContent(
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

            PreferLocationBottomSheet(
                visible = showSortSheet,
                query = query,
                onQueryChange = { viewmodel.searchLocation(it) },   // ✅ ViewModel 연결
                onDismiss = { showSortSheet = false },
                results = results                                   // ✅ 결과 리스트 전달
            )
        }
    }
}

@Composable
private fun PreferLocationStudyScreenContent(
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
            text = "내 지역 스터디",
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
            Box(
                Modifier.fillMaxSize(),
            ) {
                EmptyAlertWithButton(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(R.drawable.location_outline),
                    alertTitle = "내 지역이 아직 없어요!",
                    alertDes = "내 지역을 설정하고 스터디를 모아봐요.",
                    buttonText = "내 지역 설정하기",
                    onClick = onOpenSortSheet
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

