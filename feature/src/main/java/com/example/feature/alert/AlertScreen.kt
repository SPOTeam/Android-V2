package com.example.feature.alert

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.core.data.alert.AlertItem
import com.example.core.data.alert.AlertUiState
import com.example.core.data.global.AlertKind
import com.example.core.ui.R
import com.example.core.ui.component.appBar.BackTopBar
import com.example.core.ui.component.empty.EmptyAlert
import com.example.core.ui.shapes.ShapeBox
import com.example.core.ui.shapes.ShapeImageBox
import com.example.core.ui.shapes.ShapeImageWithBadge
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.B100
import com.example.core.ui.theme.B200
import com.example.core.ui.theme.B400
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.Black
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.SpotTypography
import com.example.core.ui.theme.White
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AlertScreen(
    navController: NavController,
    viewModel: AlertViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // ✅ getBackStackEntry는 remember로 감싸기 (navController가 키)
    val alertEntry = remember(navController) {
        navController.getBackStackEntry("알림")
    }

    // ✅ lifecycleOwner를 명시적으로 전달 (경고/수명 문제 예방)
    val scrollSignal by alertEntry.savedStateHandle
        .getStateFlow("alert_scroll_to_top", 0L)
        .collectAsStateWithLifecycle(lifecycleOwner = alertEntry)

    LaunchedEffect(scrollSignal) {
        if (scrollSignal != 0L) {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    AlertScreenContent(
        uiState = uiState,
        onBack = { navController.popBackStack() },
        onClickAppliedStudyCard = { viewModel.onClickAppliedStudyCard() },
        onClickAlert = { item -> viewModel.onClickAlert(item) },
        listState = listState
    )
}

@Composable
fun AlertRow(
    data : AlertItem,
    onClick: (AlertItem) -> Unit
) {
    if(data.kind == AlertKind.POPULAR_POST) {
        PopularPostAlert(data = data, onClick = onClick)
    } else {
        StudyNotiAlert(data = data, onClick = onClick)
    }
}

@Composable
fun EnrollStudyCard(
    modifier: Modifier = Modifier,
    isAvailable : Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // isAvailable 여부 + press 상태에 따라 색상 결정
    val containerColor = when {
        isPressed -> B200
        isAvailable -> B100
        else -> White
    }

    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
        interactionSource = interactionSource,
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.success_default),
                colorFilter = ColorFilter.tint(B500),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column() {
                Text(
                    text = "신청 스터디",
                    style = SpotTypography.bodyMedium600,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "신청 스터디의 수락 알림입니다.\n클릭하여 스터디 참여를 확인해주세요.",
                    style = SpotTypography.bodyMedium500,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Black
                )
            }
        }
    }
}

@Composable
fun PopularPostAlert(
    modifier: Modifier  = Modifier,
    data: AlertItem,
    onClick: (AlertItem) -> Unit // ✅ AlertItem 넘겨주도록 변경
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        isPressed -> B200
        data.isRead -> White
        else -> B100
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
                    indication = null, // ripple 없애려면 null
                    onClick = {
                        onClick(data.copy(isRead = true)) // ✅ 클릭 시 true로 바꾼 값 전달
                    }
                ),
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
                    backgroundColor = White,
                    borderWidth = 0.5.dp,
                    borderColor = G300,
                )

                Spacer(Modifier.width(15.dp))

                Column (
                    modifier = Modifier.weight(1f)
                )  {
                    Text(
                        text = "실시간 인기글",
                        style = SpotTypography.bodyMedium500,
                        maxLines = 1,
                        fontSize = 15.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = data.title,
                        style = SpotTypography.bodyMedium500,
                        color = Black,
                        maxLines = 1,
                        fontSize = 15.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!data.isRead) {
                    NewBadge()
                }
            }
        }
    }
}

@Composable
fun StudyNotiAlert(
    modifier: Modifier  = Modifier,
    data: AlertItem,
    onClick: (AlertItem) -> Unit // ✅ AlertItem 넘겨주도록 변경
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        isPressed -> B200
        data.isRead -> White
        else -> B100

    }

    val (primary, secondary) = when (data.kind) {
        AlertKind.STUDY_NOTICE -> "${data.title} '공지' 업데이트" to "\"${data.title}\"의 새로운 공지"
        AlertKind.STUDY_SCHEDULE -> "${data.title} '새 일정' 등록" to "\"${data.title}\"의 새로운 일정"
        AlertKind.TODO_DONE -> "${data.title} '할 일' 완료" to "\"${data.title}\"의 TODO가 완료되었어요"
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
                    indication = null,
                    onClick = {
                        onClick(data) // ✅ 클릭 시 true로 바꾼 값 전달
                    }
                ),
            shape = SpotShapes.Hard,
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShapeImageWithBadge(
                    painter = painterResource(
                        if(data.studyImageRes != null) {
                            data.studyImageRes!!
                        } else {
                            R.drawable.spot_logo
                        }
                    ),
                    shape = SpotShapes.Soft,
                    size = 55.dp,
                )

                Spacer(Modifier.width(12.dp))

                Column (
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = primary,
                        style = SpotTypography.bodyMedium500,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = secondary,
                        style = SpotTypography.bodyMedium500,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!data.isRead) {
                    NewBadge()
                }
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
        color = B400,
        modifier = modifier.size(28.dp)       // 크기 조절
    ) {
        Text(
            text = "N",
            color = White,
            style = SpotTypography.bodyMedium500,
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
    onClickAlert: (AlertItem) -> Unit,
    listState: LazyListState // ✅ 추가
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0), // ✅ 부모가 인셋 꺼놨으니 자식에서도 명시
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
                    .windowInsetsPadding(WindowInsets.navigationBars), // ✅ 하단 SafeArea
            ) {
                if (uiState.showAppliedStudyCard) {
                    item(key = "applied_card") {
                        EnrollStudyCard(
                            isAvailable = true,
                            onClick = onClickAppliedStudyCard
                        )
                    }
                }
                items(items = uiState.alerts, key = { it.id }) { item ->
                    AlertRow(
                        data = item,
                        onClick = onClickAlert
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = G300,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

