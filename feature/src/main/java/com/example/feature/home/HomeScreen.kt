package com.example.feature.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.core.data.global.QuickMenuType
import com.example.core.data.global.WeatherType
import com.example.core.data.home.QuickMenuItem
import com.example.core.data.study.StudyItem
import com.example.core.ui.R
import com.example.core.ui.component.appBar.AppBarHome
import com.example.core.ui.component.study.StudyListItem
import com.example.core.ui.component.weather.WeatherCard
import com.example.core.ui.shapes.SpotShapes

import com.example.core.ui.theme.B500
import com.example.core.ui.theme.Black
import com.example.core.ui.theme.SpotTypography
import java.time.LocalTime

val quickItems = listOf(
    QuickMenuItem(R.drawable.prefer_location, "내 지역",   QuickMenuType.REGION),
    QuickMenuItem(R.drawable.heart_clear,     "내 관심사", QuickMenuType.INTERESTS),
    QuickMenuItem(R.drawable.recruiting,      "모집 중",   QuickMenuType.RECRUITING),
    QuickMenuItem(R.drawable.bulletin_board,  "게시판",    QuickMenuType.BOARD),
)

@Composable
fun HomeScreen(
    navController: NavController,
    viewmodel: HomeScreenViewModel = hiltViewModel(),
    onStudyClick: (StudyItem) -> Unit = {}
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        temperature = state.weatherTemp,
        weatherType = state.weatherType,
        currentTime = state.currentTime,
        popularStudies = state.popularStudies,
        recommendedStudies = state.recommendedStudies,

        onQuickMenuClick = { type ->
            when (type) {
                QuickMenuType.REGION     -> navController.navigate("내 지역")
                QuickMenuType.INTERESTS  -> navController.navigate("내 관심사")
                QuickMenuType.RECRUITING -> navController.navigate("모집 중")
                QuickMenuType.BOARD      -> navController.navigate("게시판")
            }
        }
    )
}

@Composable
fun QuickMenu(
    items: List<QuickMenuItem>,
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    onItemClick: (QuickMenuType) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .clickable { onItemClick(item.type) },   // ← 식별자만 전달
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(item.iconRes),
                    contentDescription = item.label,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = item.label,
                    style = SpotTypography.bodyMedium600,
                    fontSize = 14.sp,
                    color = Black,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun PopularPostNow(
    title: String,
    subtitle: String,
    selected: Boolean = false,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onContentClick: () -> Unit = { },
    onMoreClick: () -> Unit = {},
    @DrawableRes trailingIconRes: Int = R.drawable.arrow_right,
    itemShape: Shape = SpotShapes.Hard,

    // 상태별 컨테이너 색상 커스터마이즈
    idleContainerColor: Color = Color.Transparent,
    selectedContainerColor: Color = B500.copy(alpha = 0.08f),
    pressedContainerColor: Color = B500.copy(alpha = 0.14f),


) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val bg by animateColorAsState(
        targetValue = when {
            pressed  -> pressedContainerColor
            selected -> selectedContainerColor
            else     -> idleContainerColor
        },
        label = "popularPostNowBg"
    )

    Row(
        modifier = modifier
            .clip(itemShape)
            .fillMaxHeight()
            .background(bg, itemShape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                role = Role.Button,
                onClick = onCardClick
            ),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = SpotTypography.bodyMedium500.copy(fontSize = 16.sp),
                    color = Black
                )
                Spacer(Modifier.width(4.dp))

                Icon( // fire 이미지가 벡터면 Icon, 비트맵이면 Image로 사용
                    painter = painterResource(R.drawable.fire),
                    contentDescription = "fire",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = subtitle,
                style = SpotTypography.bodySmall500.copy(fontSize = 14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable(onClick = onContentClick),
                color = Black
            )
        }

        IconButton(
            onClick = onMoreClick,
            modifier = Modifier.size(18.dp)
        ) {
            Icon(
                painter = painterResource(trailingIconRes),
                contentDescription = "더보기",
                tint = B500,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/** 섹션: 지금 가장 인기있는 스터디 (StudyUi로 렌더) */
@Composable
fun PopularStudyNow(
    items: List<StudyItem>,
    modifier: Modifier = Modifier,
    title: String = "지금 가장 인기있는 스터디",
    onMoreClick: () -> Unit = {},
    onItemClick: (StudyItem) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = SpotTypography.bodyMedium500.copy(fontSize = 18.sp),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onMoreClick, modifier = Modifier.size(18.dp)) {
                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = "더보기",
                    tint = B500
                )
            }
        }

        items.forEach { item ->
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

@Composable
fun RecommendStudyNow(
    items: List<StudyItem>,
    modifier: Modifier = Modifier,
    title: String = "당신을 기다리는 추천 스터디",
    onRefreshClick: () -> Unit = {},
    onItemClick: (StudyItem) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = SpotTypography.bodyMedium500.copy(fontSize = 18.sp),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRefreshClick, modifier = Modifier.size(18.dp)) {
                Icon(
                    painter = painterResource(R.drawable.refresh),
                    contentDescription = "새로고침"
                )
            }
        }

        items.forEach { item ->
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

@Composable
fun HomeScreenContent(
    temperature: Int?,
    weatherType: WeatherType?,
    currentTime: LocalTime?,

    onQuickMenuClick: (QuickMenuType) -> Unit,

    popularStudies: List<StudyItem>,
    recommendedStudies: List<StudyItem>,

) {
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            AppBarHome(
                hasNotification = false,
                onSearchClick = { /* TODO */ },
                onNotificationClick = { /* TODO */ }
            )
        },
    ) { innerPadding ->
        val topInsets = innerPadding.calculateTopPadding()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topInsets)
                .padding(horizontal = 17.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 상단: 날씨 + 실시간 인기글
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WeatherCard(
                        temperature = requireNotNull(temperature),
                        weatherType = requireNotNull(weatherType),
                        currentTime = requireNotNull(currentTime),
                    )

                    PopularPostNow(
                        title = "실시간 인기글",
                        subtitle = "sample 들어갈 예정sample 들어갈 예정sample 들어갈 예정",
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        onContentClick = { /* subtitle 클릭 */ },
                        onMoreClick = { /* > 아이콘 클릭 */ },
                        onCardClick = { }
                    )
                }
            }

            item {
                QuickMenu(
                    items = quickItems,
                    onItemClick = onQuickMenuClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 지금 가장 인기 있는 스터디 섹션
            item {
                PopularStudyNow(
                    items = popularStudies.map { it },
                    modifier = Modifier.fillMaxWidth(),
                    onMoreClick = { /* 전체보기 */ },
                    onItemClick = { /* 아이템 클릭 */ }
                )
            }

            // 당신을 기다리는 추천 스터디 섹션
            item {
                RecommendStudyNow(
                    items = popularStudies.map { it },
                    modifier = Modifier.fillMaxWidth(),
                    onRefreshClick = { /* 새로고침 */ },
                    onItemClick = { /* 아이템 클릭 */ }
                )
            }
        }
    }
}

