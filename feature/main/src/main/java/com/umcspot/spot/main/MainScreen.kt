package com.umcspot.spot.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umcspot.spot.alert.navigation.Alert
import com.umcspot.spot.alert.navigation.AppliedAlert
import com.umcspot.spot.alert.navigation.navigateToAlert
import com.umcspot.spot.designsystem.component.appBar.AppBarHome
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.feature.board.navigation.navigateToBoard
import com.umcspot.spot.main.component.MainBottomBar
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(navigator: MainNavigator = rememberMainNavigator()) {
    val navController = navigator.navController
    val backStackEntry by navController.currentBackStackEntryAsState()
    val dest = backStackEntry?.destination

    // 알림 화면(그래프)에 있는지 판별 (프로젝트 라우팅 규칙에 맞게 하나 택)
    val showBackToPBar =
        dest?.hierarchy?.any {
            it.hasRoute(Alert::class) || it.hasRoute(AppliedAlert::class)
        } == true

    Scaffold(
        topBar = {
            if (showBackToPBar) {
                // 라우트에 따라 타이틀 분기(선택)
                val title =
                    when {
                        dest?.hasRoute(Alert::class) == true -> "알림"
                        dest?.hasRoute(AppliedAlert::class) == true -> "신청한 알림"
                        else -> ""
                    }

                BackTopBar(
                    title = title,
                    onBackClick = { navController.popBackStack() },
                    modifier = Modifier
                        .statusBarsPadding()
                )
            } else {
                AppBarHome(
                    hasAlert = true,
                    onSearchClick = { /* TODO */ },
                    onAlertClick = { navController.navigateToAlert() },
                    modifier = Modifier
                        .statusBarsPadding()
                )
            }
        },
        bottomBar = {
            MainBottomBar(
                visible = navigator.showBottomBar(),
                tabs = MainNavTab.entries.toImmutableList(),
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigate,
            )
        },
        modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
    ) { innerPadding ->
        MainNavHost(
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding), // 중복 인셋 방지 ,
            contentPadding =  innerPadding          // ✅ 오타 수정
        )
    }
}