package com.umcspot.spot.alert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.alert.model.AppliedAlertInfo
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.empty.EmptyAlertWithButton
import com.umcspot.spot.designsystem.component.study.EnrollStudyListItem
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun EnrolledAlertScreen(
    viewModel: AlertViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onMoveToStudyScreenClick : () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    // ✅ 진입 시 데이터 로드
    LaunchedEffect(Unit) { viewModel.load() }

    LaunchedEffect(Unit) {
        onRegisterScrollToTop {
            scope.launch {
                // 아이템 0으로 부드럽게 스크롤
                listState.animateScrollToItem(0)
            }
        }
    }

    when (val state = uiState.applied) {
        is UiState.Loading -> Text("로딩 중...", color = Color.Gray)

        is UiState.Failure -> Text("에러: ${state.msg}", color = Color.Red)

        is UiState.Empty -> {
            EmptyAlertWithButton(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPad, bottom = bottomPad),
                painter = painterResource(R.drawable.study_default),
                alertTitle = "신청한 스터디가 아직 없어요!",
                alertDes = "스팟에서 내 목표를 이뤄봐요.",
                buttonText = "스터디 둘러보기",
                onClick = onMoveToStudyScreenClick
            )
        }

        is UiState.Success -> {
            val alerts: List<AppliedAlertInfo> = state.data.alerts

            EnrolledAlertScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white)
                    .padding(top = topPad, bottom = bottomPad),
                alerts = alerts,
                listState = listState,
                onAccept = { info -> viewModel.onAcceptClick(info) },
                onReject = { info -> viewModel.onRejectClick(info) }
            )
        }
    }
}

/* ------------------------ Content ------------------------ */

@Composable
fun EnrolledAlertScreenContent(
    modifier: Modifier = Modifier,
    alerts: List<AppliedAlertInfo>,
    listState: LazyListState,
    onAccept: (AppliedAlertInfo) -> Unit,
    onReject: (AppliedAlertInfo) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(
            items = alerts,
            key = { it.id }
        ) { item ->
            EnrollStudyListItem(
                modifier = Modifier.padding(10.dp),
                item = item,
                onRejectClick = { onReject(item) },
                onAcceptClick = { onAccept(item) }
            )
        }
    }
}

