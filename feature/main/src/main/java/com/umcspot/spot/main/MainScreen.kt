package com.umcspot.spot.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.umcspot.spot.designsystem.component.appBar.AppBarHome
import com.umcspot.spot.main.component.MainBottomBar
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(navigator: MainNavigator = rememberMainNavigator()) {
    Scaffold(
        topBar = {
            AppBarHome(
                hasAlert = false,
                onSearchClick = { /* TODO */ },
                onAlertClick = { navigator::navigate},
                modifier = Modifier
                    .statusBarsPadding()
            )
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