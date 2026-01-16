package com.umcspot.spot.alert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.alert.model.AlertInfo
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.component.empty.EmptyAlert
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.ShapeImageBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun AlertScreen(
    viewModel: AlertViewModel = hiltViewModel(),
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onClickAlert:(Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    LaunchedEffect(Unit) { viewModel.load() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = topPad, bottom = bottomPad)
            .padding(horizontal = screenWidthDp(17.dp))
    ) {
        when (val alertsState = uiState.alerts) {
            is UiState.Success -> {
                AlertScreenContent(
                    state = alertsState.data,
                    listState = listState,
                    onClickAlert = onClickAlert
                )
            }

            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SpotSpinner(size = screenWidthDp(30.dp))
                }
            }

            is UiState.Failure, is UiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyAlert(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.alert),
                        alertTitle = "아직 알림이 없어요.",
                        alertDes = "스팟에서 내 목표를 이뤄봐요."
                    )
                }
            }
        }
    }
}

@Composable
fun AlertScreenContent(
    state: AlertResult,
    listState: LazyListState,
    onClickAlert: (Long) -> Unit,
) {
    val alerts: List<AlertInfo> = state.studies

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = alerts,
            key = { it.applicationId }
        ) { item ->
            Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

            AlertRow(data = item, onClick = onClickAlert)

            Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(17.dp)),
                color = SpotTheme.colors.G300,
                thickness = 1.dp
            )
        }

    }
}



@Composable
fun AlertRow(
    data: AlertInfo,
    onClick: (Long) -> Unit
) {
    BlankButton(
        modifier = Modifier
            .width(screenWidthDp(326.dp))
            .height(screenHeightDp(65.dp)),
        onClick = { onClick(data.studyId) },
    ) {
        Row(
            modifier = Modifier.padding(screenWidthDp(13.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShapeImageBox(
                imageRef = data.studyImageRes,
                shape = SpotShapes.Soft,
                modifier = Modifier.size(screenWidthDp(33.dp)),
                backgroundColor = SpotTheme.colors.white,
            )

            Spacer(Modifier.width(screenWidthDp(13.dp)))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${data.title} 신청이 수락되었어요!",
                    style = SpotTheme.typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "게시판에서 가입 인사를 나눠보세요!",
                    style = SpotTheme.typography.regular_400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}