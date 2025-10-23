package com.umcspot.spot.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.umcspot.spot.alert.navigation.alertGraph
import com.umcspot.spot.alert.navigation.appliedAlertGraph
import com.umcspot.spot.alert.navigation.navigateToAlert
import com.umcspot.spot.alert.navigation.navigateToAppliedAlert
import com.umcspot.spot.category.navigation.categoryGraph
import com.umcspot.spot.feature.board.navigation.boardGraph
import com.umcspot.spot.feature.board.navigation.navigateToBoard
import com.umcspot.spot.home.navigation.homeGraph
import com.umcspot.spot.jjim.navigation.jjimGraph
import com.umcspot.spot.mypage.navigation.mypageGraph
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.mystudy.navigation.myStudyGraph

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
        homeGraph(
            contentPadding = contentPadding,
            onQuickMenuClick = { type ->
                when (type) {
                    QuickMenuType.BOARD      -> navigator.navController.navigateToBoard()
                    QuickMenuType.REGION     -> { /* navigator.navController.navigate(Region) */ }
                    QuickMenuType.INTERESTS  -> { /* navigator.navController.navigate(Interests) */ }
                    QuickMenuType.RECRUITING -> { /* navigator.navController.navigate(Recruiting) */ }
                }
            }
        )
        categoryGraph()
        myStudyGraph()
        jjimGraph()
        mypageGraph()


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