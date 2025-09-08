// HomeScreen.kt
package com.example.feature.home

import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.core.ui.component.FloatingButton
import com.example.core.ui.component.appBar.AppBarHome
import com.example.core.ui.component.weather.WeatherCard
import com.example.core.ui.component.weather.WeatherType
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.SpotTypography
import java.time.LocalTime
import com.example.core.ui.R

@Composable
fun HomeScreen(
    navController: NavController,
    viewmodel: HomeScreenViewModel = hiltViewModel(),
    onStudyClick: (StudyItem) -> Unit = {}
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        isLoading = state.isLoading,
        error = state.error,
        temperature = state.weatherTemp,
        weatherType = state.weatherType,
        currentTime = state.currentTime,
        popularStudies = state.popularStudies,
        recommendedStudies = state.recommendedStudies,
        onFabClick = { /* TODO */ },
        onSeeAllPopularClick = { /* TODO */ },
        onRefreshRecommendClick = viewmodel::refreshRecommend,
        onRetryClick = viewmodel::load,
        onStudyClick = onStudyClick
    )
}

@Composable
fun PopularNowRow(
    title: String = "실시간 인기글",
    subtitle: String,
    modifier: Modifier = Modifier,
    onContentClick: () -> Unit = {},
    onMoreClick : () -> Unit = {},
    @DrawableRes trailingIconRes: Int = R.drawable.arrow_right
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            modifier = Modifier
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = SpotTypography.bodyMedium500.copy(fontSize = 20.sp),
                )
                Text(text = "🔥", fontSize = 18.sp)

            }
            Text(
                text = subtitle,
                style = SpotTypography.bodySmall500.copy(fontSize = 14.sp),
                maxLines = 1,                      // 2줄이면 2로
                overflow = TextOverflow.Ellipsis,  // 넘치면 …
                modifier = Modifier.clickable(onClick = onContentClick)
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
                modifier = Modifier.size(18.dp) // 아이콘 시각 크기
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    isLoading: Boolean,
    error: String?,
    temperature: Int?,
    weatherType: WeatherType?,
    currentTime: LocalTime?,

    popularStudies: List<StudyItem>,
    recommendedStudies: List<StudyItem>,

    onFabClick: () -> Unit,
    onSeeAllPopularClick: () -> Unit,
    onRefreshRecommendClick: () -> Unit,
    onRetryClick: () -> Unit,
    onStudyClick: (StudyItem) -> Unit,
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
                .padding(horizontal = 14.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 날씨 카드
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WeatherCard(
                        temperature = temperature!!,
                        weatherType = weatherType!!,
                        currentTime = currentTime!!,
                    )
                    PopularNowRow(
                        title = "실시간 인기글",
                        subtitle = "sample 들어갈 예정sample 들어갈 예정sample 들어갈 예정",
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        onContentClick = { /* subtitle 클릭 */ },
                        onMoreClick = { /* > 아이콘 클릭 */ }
                    )
                }
            }

            // 인기 스터디
            if (popularStudies.isNotEmpty()) {
                items(popularStudies, key = { it.id }) { study ->
                    // TODO: StudyListItem(...)
                    // StudyListItem(item = study, onClick = { onStudyClick(study) })
                }
            } else {
                item {
                    // TODO: SectionEmpty("인기 스터디가 아직 없어요.")
                }
            }

            // 추천 스터디 (새로고침 버튼은 섹션 헤더에서 onRefreshRecommendClick 호출)
            if (recommendedStudies.isNotEmpty()) {
                items(recommendedStudies, key = { it.id }) { study ->
                    // TODO: StudyListItem(...)
                    // StudyListItem(item = study, onClick = { onStudyClick(study) })
                }
            } else {
                item {
                    // TODO: SectionEmpty("맞춤 추천을 준비 중이에요.")
                }
            }
        }

    }
}

/* ── 3) 프리뷰: VM 없이도 독립적으로 미리보기 가능 ── */
@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(
        isLoading = false,
        error = null,
        temperature = 23,
        weatherType = WeatherType.SUNNY,
        currentTime = LocalTime.of(9, 41),
        popularStudies = List(2) { i -> StudyItem("p$i", "Popular #$i", "Goal $i") },
        recommendedStudies = List(2) { i -> StudyItem("r$i", "Recommend #$i", "Goal $i") },
        onFabClick = {},
        onSeeAllPopularClick = {},
        onRefreshRecommendClick = {},
        onRetryClick = {},
        onStudyClick = {}
    )
}
