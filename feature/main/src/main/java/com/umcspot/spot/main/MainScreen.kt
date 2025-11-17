package com.umcspot.spot.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umcspot.spot.alert.navigation.Alert
import com.umcspot.spot.alert.navigation.AppliedAlert
import com.umcspot.spot.alert.navigation.navigateToAlert
import com.umcspot.spot.designsystem.component.FloatingMultipleButton
import com.umcspot.spot.designsystem.component.FloatingToUpButton
import com.umcspot.spot.designsystem.component.appBar.AppBarHome
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.main.component.MainBottomBar
import com.umcspot.spot.study.recruiting.navigation.RecruitingFilter
import com.umcspot.spot.study.register.navigation.RegisterStudy
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator()
) {
    val navController = navigator.navController
    val backStackEntry by navController.currentBackStackEntryAsState()
    val dest = backStackEntry?.destination
    var scrollToTop by remember { mutableStateOf<(() -> Unit)?>(null) }

    Scaffold(
        topBar = {
            if (!navigator.isInLanding()) {
                if (navigator.showBackTopBar()) {
                    // 라우트에 따라 타이틀 분기(선택)
                    val title =
                        when {
                            dest?.hasRoute(Alert::class) == true -> "알림"
                            dest?.hasRoute(AppliedAlert::class) == true -> "신청한 알림"
                            dest?.hasRoute(RecruitingFilter::class) == true -> "모집중인 스터디"
                            dest?.hasRoute(RegisterStudy::class) == true -> "스터디 만들기"
                            else -> ""
                        }
                    BackTopBar(
                        title = title,
                        onBackClick = { navController.popBackStack() },
                        modifier = Modifier.statusBarsPadding()
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
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FabStack(
                showToTop = navigator.showToTopFab(),
                onClickToTop = { scrollToTop?.invoke() },

                showMultiple = navigator.showMultipleFab(),
                onClickMultiple = { /* TODO */ },

                spacing = 12.dp, // floatingButton 사이 간격
            )
        },
        bottomBar = {
            if(!navigator.isInLanding()) {
                MainBottomBar(
                    visible = navigator.showBottomBar(),
                    tabs = MainNavTab.entries.toImmutableList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = navigator::navigate,
                )
            }
        },
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) { innerPadding ->
        MainNavHost(
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            contentPadding =  innerPadding,
            onRegisterScrollToTop = { handler -> scrollToTop = handler }
        )
    }
}

@Composable
private fun FabStack(
    showToTop: Boolean,
    onClickToTop: () -> Unit,
    showMultiple: Boolean,
    onClickMultiple: () -> Unit,
    spacing: Dp = 12.dp,
) {
    Box(
        modifier = Modifier
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            androidx.compose.animation.AnimatedVisibility(visible = showToTop) {
                FloatingToUpButton(onClick = onClickToTop)
            }
            androidx.compose.animation.AnimatedVisibility(visible = showMultiple) {
                FloatingMultipleButton(onClick = onClickMultiple)
            }
        }
    }
}
