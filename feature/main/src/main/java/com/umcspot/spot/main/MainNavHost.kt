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
import com.umcspot.spot.feature.board.navigation.boardListGraph
import com.umcspot.spot.feature.board.navigation.navigateToBoard
import com.umcspot.spot.feature.board.navigation.navigateToBoardList
import com.umcspot.spot.feature.board.navigation.postingGraph
import com.umcspot.spot.home.navigation.homeGraph
import com.umcspot.spot.home.navigation.navigateToHome
import com.umcspot.spot.jjim.navigation.jjimGraph
import com.umcspot.spot.landing.Landing
import com.umcspot.spot.landing.landingGraph
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.mypage.navigation.mypageGraph
import com.umcspot.spot.study.my.navigation.myStudyGraph
import com.umcspot.spot.study.preferLocation.navigation.navigateToPreferLocationStudy
import com.umcspot.spot.study.preferLocation.navigation.preferLocationStudyGraph
import com.umcspot.spot.study.recruiting.navigation.navigateToRecruitingStudy
import com.umcspot.spot.study.recruiting.navigation.navigateToRecruitingStudyFilter
import com.umcspot.spot.study.recruiting.navigation.recruitingStudyFilterGraph
import com.umcspot.spot.study.recruiting.navigation.recruitingStudyGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
    contentPadding : PaddingValues = PaddingValues(0.dp),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onBackRequest: () -> Unit,
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
                    popUpTo(Landing) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            ) },
            onNaverClick = { navigator.navController.navigateToHome(
                navOptions {
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
                    QuickMenuType.BOARD      -> { navigator.navController.navigateToBoard() }
                    QuickMenuType.REGION     -> { navigator.navController.navigateToPreferLocationStudy() }
                    QuickMenuType.INTERESTS  -> { /* navigator.navController.navigate(Interests) */ }
                    QuickMenuType.RECRUITING -> { navigator.navController.navigateToRecruitingStudy() }
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
            onItemClick = { },
            onFilterClick = { navigator.navController.navigateToRecruitingStudyFilter() },
        )

        recruitingStudyFilterGraph(
            contentPadding = contentPadding,
            onAcceptFilterClick = { navigator.navController.popBackStack() }
        )

        preferLocationStudyGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = { },
            onFilterClick = {  },
        )


        boardGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onMoveToBoardList = { navigator.navController.navigateToBoardList() },
        )

        boardListGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onRegisterScrollToTop = onRegisterScrollToTop
        )

        postingGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onBackRequest = onBackRequest
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