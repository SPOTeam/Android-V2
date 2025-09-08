package com.example.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.data.global.WeatherType
import com.example.core.ui.R
import com.example.core.ui.component.FloatingButton
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.Black
import com.example.core.ui.theme.SpotTypography
import com.example.feature.home.HomeScreen
import com.example.feature.home.HomeScreenContent
import java.time.LocalTime

data class MainTab(
    val route: String,
    val iconResId: Int,
    val iconClickResId: Int
)

val items = listOf(
    MainTab("홈", R.drawable.home_default, R.drawable.home_filled),
    MainTab("카테고리", R.drawable.category_default, R.drawable.category_filled),
    MainTab("내 스터디", R.drawable.study_default, R.drawable.study_filled),
    MainTab("찜", R.drawable.like_default, R.drawable.like_filled_b400),
    MainTab("마이페이지", R.drawable.mypage_default, R.drawable.mypage_filled),
)

@Composable
fun MyBottomNavigation(navController: NavHostController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            val iconRes = if (isSelected) item.iconClickResId else item.iconResId

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = item.route,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(33.dp)
                    )
                },
                label = {
                    Text(
                        text = item.route,
                        style = SpotTypography.header02,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedTextColor = B500,
                    unselectedTextColor = Black,
                )
            )
        }
    }
}

@Composable
fun MyNavigationHost(navController: NavHostController) {

    val isPreview = LocalInspectionMode.current

    NavHost(navController, startDestination = "홈") {
        composable("홈") {
            if (isPreview) {
                HomeScreenContent(
                    temperature = 23,
                    weatherType = WeatherType.SUNNY,
                    currentTime = LocalTime.of(9, 41),
                    popularStudies = emptyList(),
                    recommendedStudies = emptyList(),
                    onSeeAllPopularClick = {},
                    onRefreshRecommendClick = {},
                    onRetryClick = {},
                    onStudyClick = {},
                    onQuickMenuClick = {},
                )
            } else {
                HomeScreen(navController)
            }
        }

        // 프리뷰/임시: Hilt 등 의존 없는 플레이스홀더로 구성
        composable("카테고리") { PlaceholderScreen("카테고리") }
        composable("내 스터디") { PlaceholderScreen("내 스터디") }
        composable("찜") { PlaceholderScreen("찜") }
        composable("마이페이지") { PlaceholderScreen("마이페이지") }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("$name 화면", style = SpotTypography.bodyMedium500)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // 프리뷰 첫 프레임에서 null일 수 있으므로 기본값을 "홈"으로
    val currentRoute = navBackStackEntry?.destination?.route ?: "홈"

    val bottomBarRoutes = items.map { it.route }

    val showFab = currentRoute == "홈"

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                MyBottomNavigation(navController)
            }
        },
        floatingActionButton = {
            if (showFab) {
                // FAB는 바텀바 바로 위에 자연스럽게 붙습니다
                FloatingButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp) // 여백만 살짝
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MyNavigationHost(navController)
        }
    }
}
