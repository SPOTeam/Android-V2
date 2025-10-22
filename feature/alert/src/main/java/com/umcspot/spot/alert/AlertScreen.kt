package com.umcspot.spot.alert

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.umcspot.spot.alert.model.AlertInfo
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
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun AlertScreen(
    viewModel: AlertViewModel = hiltViewModel(),
    scrollToTopTick: Long? = null,
    contentPadding : PaddingValues,
    onClickApplied: () -> Unit          // ✅ 추가
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    LaunchedEffect(scrollToTopTick) {
        if (scrollToTopTick != null && scrollToTopTick != 0L) {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    LaunchedEffect(Unit) { viewModel.load() }

    when (val state = uiState.general) {
        is UiState.Loading -> Text("로딩 중...", color = Color.Gray)

        is UiState.Failure -> Text("에러: ${state.msg}", color = Color.Red)

        is UiState.Empty -> {
            EmptyAlert(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPad, bottom = bottomPad),
                painter = painterResource(R.drawable.alert),
                alertTitle = "아직 알림이 없어요.",
                alertDes = "스팟에서 내 목표를 이뤄봐요."
            )
        }

        is UiState.Success -> {
            val alerts: List<AlertInfo> = state.data.alerts
            val showAppliedCard =
                uiState.hasAppliedData && (uiState.applied as? UiState.Success)?.data?.alerts?.isNotEmpty() == true

            AlertScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white)
                    .padding(top = topPad, bottom = bottomPad),
                alerts = alerts,
                showAppliedStudyCard = showAppliedCard,
                onClickAppliedStudyCard = onClickApplied,
                onClickAlert = { item -> viewModel.onClickAlert(item) },
                listState = listState
            )
        }
    }
}

@Composable
fun AlertRow(
    data: AlertInfo,
    onClick: (AlertInfo) -> Unit
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
    data: AlertInfo,
    onClick: (AlertInfo) -> Unit
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
            .padding(horizontal = 12.dp, vertical = 4.dp),
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
    data: AlertInfo,
    onClick: (AlertInfo) -> Unit
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
            .padding(horizontal = 16.dp, vertical = 4.dp),
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
                    painter = rememberImageRefPainter(
                        ref = data.studyImageRes,            // ImageRef.None / LocalName / UriRef / Url
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
    modifier: Modifier = Modifier,
    alerts: List<AlertInfo>,
    showAppliedStudyCard: Boolean,
    onClickAppliedStudyCard: () -> Unit,
    onClickAlert: (AlertInfo) -> Unit,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        if (showAppliedStudyCard) {
            item(key = "applied_card") {
                EnrollStudyCard(
                    isAvailable = true,
                    onClick = onClickAppliedStudyCard
                )
            }
        }
        items(
            items = alerts,
            key = { it.id }
        ) { item ->
            AlertRow(
                data = item,
                onClick = onClickAlert
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                color = SpotTheme.colors.G300,
                thickness = 0.5.dp
            )
        }
    }
}

/****** 유틸리티 *******/

@Composable
fun rememberImageRefPainter(
    ref: ImageRef,
    @DrawableRes fallback: Int = R.drawable.spot_logo
): Painter {
    val context = LocalContext.current
    return when (ref) {
        is ImageRef.LocalName -> {
            val id = context.resources.getIdentifier(ref.name, "drawable", context.packageName)
            painterResource(id.takeIf { it != 0 } ?: fallback)
        }
        is ImageRef.LocalPath -> rememberAsyncImagePainter(model = File(ref.path))
        is ImageRef.Url -> rememberAsyncImagePainter(model = ref.url)
        ImageRef.None -> painterResource(fallback)
    }
}

private fun Context.drawableIdByName(name: String): Int? {
    val id = resources.getIdentifier(name, "drawable", packageName)
    return if (id != 0) id else null
}

