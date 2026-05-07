package com.umcspot.spot.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.study.StudyListItem
import com.umcspot.spot.designsystem.component.weather.WeatherCard
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.home.component.PopularPostNow
import com.umcspot.spot.home.component.QuickMenu
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    contentPadding: PaddingValues,
    onQuickMenuClick: (QuickMenuType) -> Unit,
    onPopularClick: () -> Unit,
    onPopularPostClick: (Long) -> Unit,
    onStudyClick: (Long) -> Unit,
    onStudyMoreClick: () -> Unit,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.loadWithLocation(fusedClient) else viewModel.load()
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

        onRegisterScrollToTop {
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    HomeScreen(
        contentPadding = contentPadding,
        uiState = uiState,
        listState = listState,
        onQuickMenuClick = onQuickMenuClick,
        onPopularClick = onPopularClick,
        onPopularPostClick = onPopularPostClick,
        onStudyClick = onStudyClick,
        onStudyMoreClick = onStudyMoreClick,
        onRefreshRecommended = viewModel::reLoadRecommendedStudies
    )
}

@Composable
private fun HomeScreen(
    contentPadding: PaddingValues,
    uiState: HomeState,
    listState: LazyListState,
    onQuickMenuClick: (QuickMenuType) -> Unit,
    onPopularClick: () -> Unit,
    onPopularPostClick: (Long) -> Unit,
    onStudyClick: (Long) -> Unit,
    onStudyMoreClick: () -> Unit,
    onRefreshRecommended: () -> Unit
) {
    var restoreIndex by remember { mutableStateOf<Int?>(null) }
    var restoreOffset by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(uiState.recommendStudies) {
        if (uiState.recommendStudies is UiState.Success) {
            val idx = restoreIndex
            val off = restoreOffset
            if (idx != null && off != null) {
                listState.scrollToItem(idx, off)
                restoreIndex = null
                restoreOffset = null
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
            .padding(horizontal = screenWidthDp(17.dp)),
        state = listState
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(screenWidthDp(156.dp))
                        .height(screenHeightDp(79.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    when (val weather = uiState.weatherInfo) {
                        is UiState.Success -> WeatherCard(
                            temperature = weather.data.weatherTemp,
                            weatherType = weather.data.weatherType,
                            currentTime = weather.data.currentTime
                        )
                        else -> SpotSpinner()
                    }
                }

                Spacer(modifier = Modifier.width(screenWidthDp(14.dp)))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(screenHeightDp(79.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    when (val post = uiState.popularPostInfo) {
                        is UiState.Success -> PopularPostNow(
                            postInfo = post.data,
                            onContentClick = onPopularPostClick,
                            onCardClick = onPopularClick
                        )
                        else -> SpotSpinner()
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
            HomeSectionHeader(
                title = "지금 가장 인기있는 스터디",
                onIconClick = onStudyMoreClick,
                iconRes = R.drawable.arrow_right,
                iconTint = SpotTheme.colors.B500
            )
            HomeStudyList(
                state = uiState.popularStudies,
                onStudyClick = onStudyClick
            )
        }

        item {
            HomeSectionHeader(
                title = "전공/진로학습 스터디 이건 어때요",
                onIconClick = {
                    restoreIndex = listState.firstVisibleItemIndex
                    restoreOffset = listState.firstVisibleItemScrollOffset
                    onRefreshRecommended()
                },
                iconRes = R.drawable.refresh,
                iconTint = Black
            )
            HomeStudyList(
                state = uiState.recommendStudies,
                onStudyClick = onStudyClick
            )
        }
    }
}

@Composable
private fun HomeSectionHeader(
    title: String,
    onIconClick: () -> Unit,
    iconRes: Int,
    iconTint: Color
) {
    Spacer(modifier = Modifier.height(screenHeightDp(30.dp)))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = screenWidthDp(12.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = SpotTheme.typography.h3,
        )
        FilledIconButton(
            onClick = onIconClick,
            shape = SpotShapes.Hard,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = SpotTheme.colors.white,
                contentColor = Black
            ),
            modifier = Modifier.size(screenWidthDp(24.dp))
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(screenWidthDp(14.dp)),
                tint = iconTint
            )
        }
    }
    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
}

@Composable
private fun HomeStudyList(
    state: UiState<StudyResultList>,
    onStudyClick: (Long) -> Unit
) {
    when (state) {
        is UiState.Success -> {
            Column(modifier = Modifier.fillMaxWidth()) {
                state.data.studyList.forEachIndexed { index, study ->
                    StudyListItem(
                        item = study,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onStudyClick(study.id) }
                    )
                    if (index != state.data.studyList.lastIndex) {
                        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
                        HorizontalDivider(
                            color = SpotTheme.colors.G300,
                            thickness = 1.dp
                        )
                        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
                    }
                }
            }
        }
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeightDp(200.dp)),
                contentAlignment = Alignment.Center
            ) {
                SpotSpinner()
            }
        }
        else -> Unit
    }
}