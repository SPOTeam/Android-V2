package com.umcspot.spot.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.umcspot.spot.alert.navigation.alertGraph
import com.umcspot.spot.alert.navigation.appliedAlertGraph
import com.umcspot.spot.alert.navigation.navigateToAppliedAlert
import com.umcspot.spot.category.navigation.categoryGraph
import com.umcspot.spot.feature.board.navigation.boardGraph
import com.umcspot.spot.feature.board.navigation.navigateToBoard
import com.umcspot.spot.home.navigation.homeGraph
import com.umcspot.spot.home.navigation.navigateToHome
import com.umcspot.spot.jjim.navigation.jjimGraph
import com.umcspot.spot.landing.Landing
import com.umcspot.spot.landing.landingGraph
import com.umcspot.spot.mypage.navigation.mypageGraph
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.study.navigation.Recruiting
import com.umcspot.spot.study.navigation.myStudyGraph
import com.umcspot.spot.study.navigation.recruitingStudyGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
    contentPadding : PaddingValues = PaddingValues(0.dp),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit, // ✅ 추가

) {
    NavHost(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        navController = navigator.navController,
        startDestination = navigator.startDestination
    ) {
        landingGraph(
            onKakaoClick = { navigator.navController.navigateToHome(
                navOptions {
                    // Landing을 백스택에서 제거
                    popUpTo(Landing) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            ) },
            onNaverClick = { navigator.navController.navigateToHome(
                navOptions {
                    // Landing을 백스택에서 제거
                    popUpTo(Landing) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            ) }
        )

        homeGraph(
            contentPadding = contentPadding,
            onQuickMenuClick = { type ->
                when (type) {
                    QuickMenuType.BOARD      -> navigator.navController.navigateToBoard()
                    QuickMenuType.REGION     -> { /* navigator.navController.navigate(Region) */ }
                    QuickMenuType.INTERESTS  -> { /* navigator.navController.navigate(Interests) */ }
                    QuickMenuType.RECRUITING -> { navigator.navController.navigate(Recruiting) }
                }
            }
        )
        categoryGraph()
        myStudyGraph()
        jjimGraph()
        mypageGraph()


        recruitingStudyGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onFilterClick = {},
            onItemClick = {}
        )

        boardGraph(
            contentPadding = contentPadding,
        )

        alertGraph(
            contentPadding = contentPadding,
            onClickApplied = {navigator.navController.navigateToAppliedAlert()},
            onRegisterScrollToTop = onRegisterScrollToTop
        )

        appliedAlertGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onMoveToStudyScreenClick = {navigator.navController.navigateToBoard()} // Study로 수정 필요
        )


    }
}