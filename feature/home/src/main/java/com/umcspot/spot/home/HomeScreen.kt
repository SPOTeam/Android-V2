package com.umcspot.spot.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.component.button.ImageButtonState
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.component.weather.WeatherCard
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.domain.board.model.board.BestPostResult
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.model.WeatherType
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.weather.model.WeatherResult
import java.time.LocalTime

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onQuickMenuClick: (QuickMenuType) -> Unit,
    onPopularClick: () -> Unit,
    onPopularPostClick: (Long) -> Unit,
    onStudyClick: (StudyResult) -> Unit,
    onStudyMoreClick: () -> Unit,
    contentPadding: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val context = LocalContext.current
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.loadWithLocation(fusedClient)
        } else {
            viewModel.load()
        }
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            viewModel.loadWithLocation(fusedClient)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = topPad, bottom = bottomPad)
    ) {
        HomeScreenContent(
            weatherState = uiState.weatherInfo,
            popularPostState = uiState.popularPostInfo,
            popularStudiesState = uiState.popularStudies,
            recommendedStudiesState = uiState.recommendStudies,
            onPopularClick = onPopularClick,
            onPopularPostClick = onPopularPostClick,
            onQuickMenuClick = onQuickMenuClick,
            onStudyClick = onStudyClick,
            onStudyMoreClick = onStudyMoreClick,
            onRefreshRecommended = { viewModel.reLoadRecommendedStudies() }
        )
    }
}

@Composable
fun QuickMenu(
    items: List<QuickMenuType>,
    modifier: Modifier = Modifier,
    onItemClick: (QuickMenuType) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                BlankButton(
                    modifier = Modifier.size(screenWidthDp(71.dp)),
                    onClick = { onItemClick(item) }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box { // 그림자 넣기 위함
                            Image(
                                painter = getIconForType(item),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    SpotTheme.colors.black.copy(alpha = 0.15f)
                                ),
                                modifier = Modifier
                                    .size(screenWidthDp(31.dp))
                                    .graphicsLayer {
                                        translationY = 1.dp.toPx()
                                        renderEffect = BlurEffect(9f, 9f)
                                        clip = false
                                    }
                            )
                            Image(
                                painter = getIconForType(item),
                                contentDescription = null,
                                modifier = Modifier.size(screenWidthDp(31.dp))
                            )
                        }
                        Spacer(Modifier.height(screenHeightDp(7.dp)))
                        Text(
                            text = item.label,
                            style = SpotTheme.typography.small_500,
                            color = Black,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PopularPostNow(
    postInfo: BestPostResult,
    onCardClick: () -> Unit = {},
    onContentClick: (Long) -> Unit = { },
) {
    BlankButton(
        modifier = Modifier
            .width(screenWidthDp(156.dp))
            .height(screenHeightDp(79.dp)),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = screenWidthDp(8.dp))
                .matchParentSize(),
            verticalArrangement = Arrangement.spacedBy(
                screenHeightDp(7.dp),
                Alignment.CenterVertically
            )
        ) {
            Row(
                modifier = Modifier.padding(start = screenWidthDp(4.dp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "실시간 인기글",
                    style = SpotTheme.typography.h5
                )
                Box { // 그림자 넣기 위함
                    Image(
                        painter = painterResource(R.drawable.fire),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            SpotTheme.colors.black.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier
                            .size(screenWidthDp(20.dp))
                            .graphicsLayer {
                                translationY = 1.dp.toPx()
                                renderEffect = BlurEffect(12f, 12f)
                                clip = false
                            }
                    )
                    Image(
                        painter = painterResource(R.drawable.fire),
                        contentDescription = null,
                        modifier = Modifier.size(screenWidthDp(20.dp))
                    )
                }
            }

            BlankButton(
                modifier = Modifier
                    .width(screenWidthDp(140.dp))
                    .height(screenHeightDp(22.dp)),
                state = ImageButtonState.XOUTLINETransparentState,
                onClick = { onContentClick(postInfo.postId) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = screenWidthDp(4.dp), vertical = screenHeightDp(2.dp))
                        .matchParentSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = postInfo.title,
                        style = SpotTheme.typography.regular_500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Black
                    )

                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = "더보기",
                        tint = SpotTheme.colors.B500,
                        modifier = Modifier.size(screenWidthDp(17.dp))
                    )
                }
            }
        }
    }
}



/** 섹션: 지금 가장 인기있는 스터디 (StudyUi로 렌더) */
@Composable
fun PopularStudyNow(
    items: List<StudyResult>,
    modifier: Modifier = Modifier,
    onItemClick: (StudyResult) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        items.forEach { item ->
            Spacer(Modifier.padding(screenHeightDp(4.dp)))
            StudyListItem(
                item = item,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onItemClick(item) }
            )

            if (item != items.last()) {
                Spacer(Modifier.padding(screenHeightDp(4.dp)))

                HorizontalDivider(
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun RecommendStudyNow(
    items: List<StudyResult>,
    modifier: Modifier = Modifier,
    onItemClick: (StudyResult) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        items.forEach { item ->
            Spacer(Modifier.padding(screenHeightDp(4.dp)))

            StudyListItem(
                item = item,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onItemClick(item) }
            )

            if (item != items.last()) {
                Spacer(Modifier.padding(screenHeightDp(4.dp)))

                HorizontalDivider(
                    color = SpotTheme.colors.G300,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    weatherState: UiState<WeatherResult>,
    popularPostState: UiState<BestPostResult>,
    popularStudiesState: UiState<StudyResultList>,
    recommendedStudiesState: UiState<StudyResultList>,
    onPopularClick: () -> Unit,
    onPopularPostClick: (Long) -> Unit,
    onQuickMenuClick: (QuickMenuType) -> Unit,
    onStudyClick: (StudyResult) -> Unit,
    onStudyMoreClick: () -> Unit,
    onRefreshRecommended: () -> Unit,
) {
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    var restoreIndex by remember { mutableStateOf<Int?>(null) }
    var restoreOffset by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(recommendedStudiesState) {
        if (recommendedStudiesState is UiState.Success) {

            val idx = restoreIndex
            val off = restoreOffset
            if (idx != null && off != null) {
                listState.scrollToItem(idx, off)
                restoreIndex = null
                restoreOffset = null
            }
        }
    }

    val (temperature, weatherType, currentTime) = when (weatherState) {
        is UiState.Success -> Triple(
            weatherState.data.weatherTemp,
            weatherState.data.weatherType,
            weatherState.data.currentTime
        )

        else -> Triple(null, null, LocalTime.now())
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = screenWidthDp(17.dp)),
        state = listState
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth()) {

                when (weatherState) {
                    is UiState.Success -> {
                        WeatherCard(
                            temperature = weatherState.data.weatherTemp,
                            weatherType = weatherState.data.weatherType,
                            currentTime = weatherState.data.currentTime
                        )
                    }

                    // 로딩/실패/Empty => 빈 영역(=아무것도 안 그림)
                    is UiState.Empty, is UiState.Loading, is UiState.Failure -> {
                        // WeatherCard 자리만큼 공간을 유지하고 싶으면 Spacer로 자리만 잡아주면 됨
                        Box(
                            modifier = Modifier
                                .width(screenWidthDp(156.dp))   // WeatherCard 실제 폭으로 맞춰줘
                                .height(screenHeightDp(79.dp)), // WeatherCard 실제 높이로 맞춰줘
                            contentAlignment = Alignment.Center
                        ) {
                            SpotSpinner()
                        }
                    }
                }

                Spacer(modifier = Modifier.width(screenWidthDp(14.dp)))

                when (popularPostState) {
                    is UiState.Success -> {
                        PopularPostNow(
                            postInfo = popularPostState.data,
                            onContentClick = { post -> onPopularPostClick( post) },
                            onCardClick = onPopularClick
                        )
                    }

                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(screenHeightDp(79.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            SpotSpinner()
                        }
                    }

                    is UiState.Failure -> {
                        BlankButton(
                            modifier = Modifier
                                .padding(horizontal = screenWidthDp(8.dp))
                                .width(screenWidthDp(156.dp))
                                .height(screenHeightDp(79.dp)),
                            onClick = onPopularClick
                        ) {
                            Text(
                                "불러오지 못했어요",
                                style = SpotTheme.typography.regular_500,
                                color = Black
                            )
                        }
                    }

                    UiState.Empty -> {
                        BlankButton(
                            modifier = Modifier
                                .padding(horizontal = screenWidthDp(8.dp))
                                .width(screenWidthDp(156.dp))
                                .height(screenHeightDp(79.dp)),
                            onClick = onPopularClick
                        ) {
                            Text(
                                "인기글이 없어요",
                                style = SpotTheme.typography.regular_500,
                                color = Black
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
            QuickMenu(
                items = QuickMenuType.entries,
                onItemClick = onQuickMenuClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(screenHeightDp(30.dp)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = screenWidthDp(12.dp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "지금 가장 인기있는 스터디",
                    style = SpotTheme.typography.h3,
                )

                FilledIconButton(
                    onClick = onStudyMoreClick,
                    shape = SpotShapes.Hard,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = SpotTheme.colors.white,
                        contentColor = Black
                    ),
                    modifier = Modifier.size(screenWidthDp(24.dp))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = "더보기",
                        modifier = Modifier.size(screenWidthDp(14.dp)),
                        tint = SpotTheme.colors.B500
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
            when (popularStudiesState) {
                is UiState.Success -> {
                    PopularStudyNow(
                        items = popularStudiesState.data.studyList,
                        modifier = Modifier.fillMaxWidth(),
                        onItemClick = onStudyClick
                    )
                }

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeightDp(287.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }
                }
                is UiState.Failure -> Text("인기 스터디를 불러오지 못했어요", color = Color.Red)
                UiState.Empty -> Text("인기 스터디가 없어요")
            }
        }

        item {
            Spacer(modifier = Modifier.height(screenHeightDp(30.dp)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = screenWidthDp(12.dp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "전공/진로학습 스터디 이건 어때요",
                    style = SpotTheme.typography.h3,
                )
                FilledIconButton(
                    onClick = {
                        restoreIndex = listState.firstVisibleItemIndex
                        restoreOffset = listState.firstVisibleItemScrollOffset
                        onRefreshRecommended()
                    },
                    shape = SpotShapes.Hard,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = SpotTheme.colors.white,
                        contentColor = Black
                    ),
                    modifier = Modifier.size(screenWidthDp(24.dp))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.refresh),
                        contentDescription = "더보기",
                        modifier = Modifier.size(screenWidthDp(14.dp)),
                        tint = SpotTheme.colors.black
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

            when (recommendedStudiesState) {
                is UiState.Success -> {
                    RecommendStudyNow(
                        items = recommendedStudiesState.data.studyList,
                        modifier = Modifier.fillMaxWidth(),
                        onItemClick = onStudyClick
                    )
                }

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeightDp(287.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }
                }

                is UiState.Failure -> {
                    Column {
                        Text("추천 스터디를 불러오지 못했어요", color = Color.Red)
                        Spacer(Modifier.height(8.dp))
                        IconButton(onClick = onRefreshRecommended) {
                            Icon(
                                painter = painterResource(R.drawable.refresh),
                                contentDescription = "새로고침"
                            )
                        }
                    }
                }

                UiState.Empty -> Text("추천 스터디가 없어요")
            }
        }
    }
}

@Composable
fun getIconForType(type: QuickMenuType) = when (type) {
    QuickMenuType.REGION -> painterResource(R.drawable.prefer_location)
    QuickMenuType.INTERESTS -> painterResource(R.drawable.heart_clear)
    QuickMenuType.RECRUITING -> painterResource(R.drawable.recruiting)
    QuickMenuType.BOARD -> painterResource(R.drawable.bulletin_board)
}

