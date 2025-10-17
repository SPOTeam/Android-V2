package com.umcspot.spot.alert

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.empty.EmptyAlert
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.ShapeImageBox
import com.umcspot.spot.designsystem.shapes.ShapeImageWithBadge
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.AlertKind
import kotlinx.coroutines.launch

@Composable
fun AlertScreen(
    viewModel: AlertViewModel = hiltViewModel(),
    scrollToTopTick: Long? = null,
    contentPadding : PaddingValues
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(scrollToTopTick) {
        if (scrollToTopTick != null && scrollToTopTick != 0L) {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    AlertScreenContent(
        uiState = uiState,
        onBack = { /* TODO: NavController.popBackStack() 등 연결 */ },
        onClickAppliedStudyCard = { viewModel.onClickAppliedStudyCard() },
        onClickAlert = { item -> viewModel.onClickAlert(item) },
        listState = listState
    )
}

@Composable
fun AlertRow(
    data: AlertResult,
    onClick: (AlertResult) -> Unit
) {
    if (data.kind == AlertKind.POPULAR_POST) {
        PopularPostAlert(data = data, onClick = onClick)
    } else {
        StudyNotiAlert(data = data, onClick = onClick)
    }
}

@Composable
fun EnrollStudyCard(
    modifier: Modifier = Modifier,
    isAvailable: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // isAvailable 여부 + press 상태에 따라 색상 결정 (테마 컬러로 통일)
    val containerColor = when {
        isPressed -> SpotTheme.colors.B200
        isAvailable -> SpotTheme.colors.B100
        else -> SpotTheme.colors.white
    }

    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
        interactionSource = interactionSource,
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.success_default),
                colorFilter = ColorFilter.tint(SpotTheme.colors.B500),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "신청 스터디",
                    style = SpotTheme.typography.bodyMedium600,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "신청 스터디의 수락 알림입니다.\n클릭하여 스터디 참여를 확인해주세요.",
                    style = SpotTheme.typography.bodyMedium500,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = SpotTheme.colors.Black
                )
            }
        }
    }
}

@Composable
fun PopularPostAlert(
    modifier: Modifier = Modifier,
    data: AlertResult,
    onClick: (AlertResult) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        isPressed -> SpotTheme.colors.B200
        data.isRead -> SpotTheme.colors.white
        else -> SpotTheme.colors.B100
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, // ripple 없앰
                ) { onClick(data) }, // 읽음 처리(상태 변경)는 ViewModel에서
            shape = SpotShapes.Hard,
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShapeImageBox(
                    painter = painterResource(R.drawable.fire),
                    shape = SpotShapes.Soft,
                    size = 55.dp,
                    backgroundColor = SpotTheme.colors.white,
                    borderWidth = 0.5.dp,
                    borderColor = SpotTheme.colors.G300,
                )

                Spacer(Modifier.width(15.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "실시간 인기글",
                        style = SpotTheme.typography.bodyMedium500,
                        maxLines = 1,
                        fontSize = 15.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = data.title,
                        style = SpotTheme.typography.bodyMedium500,
                        color = SpotTheme.colors.Black,
                        maxLines = 1,
                        fontSize = 15.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!data.isRead) NewBadge()
            }
        }
    }
}

@Composable
fun StudyNotiAlert(
    modifier: Modifier = Modifier,
    data: AlertResult,
    onClick: (AlertResult) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        isPressed -> SpotTheme.colors.B200
        data.isRead -> SpotTheme.colors.white
        else -> SpotTheme.colors.B100
    }

    val (primary, secondary) = when (data.kind) {
        AlertKind.STUDY_NOTICE ->
            "${data.title} '공지' 업데이트" to "\"${data.title}\"의 새로운 공지"
        AlertKind.STUDY_SCHEDULE ->
            "${data.title} '새 일정' 등록" to "\"${data.title}\"의 새로운 일정"
        AlertKind.TODO_DONE ->
            "${data.title} '할 일' 완료" to "\"${data.title}\"의 TODO가 완료되었어요"
        else -> "" to ""
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onClick(data) },
            shape = SpotShapes.Hard,
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShapeImageWithBadge(
                    painter = painterResource(
                        data.studyImageRes ?: R.drawable.spot_logo
                    ),
                    shape = SpotShapes.Soft,
                    size = 55.dp,
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = primary,
                        style = SpotTheme.typography.bodyMedium500,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = secondary,
                        style = SpotTheme.typography.bodyMedium500,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!data.isRead) NewBadge()
            }
        }
    }
}

@Composable
fun NewBadge(
    modifier: Modifier = Modifier
) {
    ShapeBox(
        shape = SpotShapes.Hard,
        color = SpotTheme.colors.B400,
        modifier = modifier.size(28.dp)
    ) {
        Text(
            text = "N",
            color = SpotTheme.colors.white,
            style = SpotTheme.typography.bodyMedium500,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AlertScreenContent(
    uiState: AlertUiState,
    onBack: () -> Unit,
    onClickAppliedStudyCard: () -> Unit,
    onClickAlert: (AlertResult) -> Unit,
    listState: LazyListState
) {
    // 기본 인셋을 끄고, 상/하단 인셋은 각 영역에서 명시적으로 처리
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Box(Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                BackTopBar(
                    title = "알림",
                    onBackClick = onBack
                )
            }
        }
    ) { innerPadding ->
        if (uiState.alerts.isEmpty()) {
            EmptyAlert(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                painter = painterResource(R.drawable.alert),
                alertTitle = "아직 알림이 없어요.",
                alertDes = "스팟에서 내 목표를 이뤄봐요."
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.navigationBars), // 하단 SafeArea
            ) {
                if (uiState.showAppliedStudyCard) {
                    item(key = "applied_card") {
                        EnrollStudyCard(
                            isAvailable = true,
                            onClick = onClickAppliedStudyCard
                        )
                    }
                }
                items(
                    items = uiState.alerts,
                    key = { it.id }
                ) { item ->
                    AlertRow(
                        data = item,
                        onClick = onClickAlert
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = SpotTheme.colors.G300,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}
