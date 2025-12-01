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
import com.umcspot.spot.category.navigation.categoryGraph
import com.umcspot.spot.checkList.navigation.checkListGraph
import com.umcspot.spot.feature.board.boardList.navigation.boardListGraph
import com.umcspot.spot.feature.board.boardList.navigation.navigateToBoardList
import com.umcspot.spot.feature.board.main.navigation.boardGraph
import com.umcspot.spot.home.navigation.homeGraph
import com.umcspot.spot.jjim.navigation.jjimGraph
import com.umcspot.spot.landing.navigation.landingGraph
import com.umcspot.spot.landing.navigation.navigateToSaving
import com.umcspot.spot.landing.navigation.savingGraph
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.mypage.navigation.mypageGraph
import com.umcspot.spot.post.content.navigation.navigateToPostContent
import com.umcspot.spot.post.content.navigation.postContentGraph
import com.umcspot.spot.post.posting.navigation.postingGraph
import com.umcspot.spot.signup.navigation.signupGraph
import com.umcspot.spot.study.my.navigation.myStudyGraph
import com.umcspot.spot.study.preferLocation.navigation.preferLocationStudyGraph
import com.umcspot.spot.study.recruiting.navigation.recruitingStudyFilterGraph
import com.umcspot.spot.study.recruiting.navigation.recruitingStudyGraph
import com.umcspot.spot.study.register.navigation.registerStudyGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onBackRequest : () -> Unit
) {
    val clearStackNavOptions = navOptions {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
        restoreState = false
    }
    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        landingGraph(
            onLoginSuccess = {
                navigator.navigateToSignUp(clearStackNavOptions)
            }
        )

        signupGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onNextClick = { navigator.navigateToCheckList() }
        )

        checkListGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onNextClick = { navigator.navController.navigateToSaving() }
        )

        savingGraph(
            contentPadding = contentPadding,
            onFinished = {
                navigator.navigateToHome(clearStackNavOptions)
            }
        )

        homeGraph(
            contentPadding = contentPadding,
            onQuickMenuClick = { type ->
                when (type) {
                    QuickMenuType.BOARD -> navigator.navigateToBoard()
                    QuickMenuType.REGION -> navigator.navigateToPreferLocationStudy()
                    QuickMenuType.INTERESTS -> { /* TODO */
                    }

                    QuickMenuType.RECRUITING -> navigator.navigateToRecruitingStudy()
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
            onFilterClick = { navigator.navigateToRecruitingStudyFilter() },
        )

        recruitingStudyFilterGraph(
            contentPadding = contentPadding,
            onAcceptFilterClick = { navigator.popBackStack() }
        )

        preferLocationStudyGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = { },
            onFilterClick = { },
        )

        boardGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onMoveToBoardList = { navigator.navController.navigateToBoardList() },
        )

        boardListGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onPostClick = { navigator.navController.navigateToPostContent() }
        )

        postingGraph(
            contentPadding = contentPadding,
//            navController = navigator.navController,
            onBackRequest = onBackRequest
        )

        postContentGraph(
            contentPadding = contentPadding,
            navController = navigator.navController
        )

        alertGraph(
            contentPadding = contentPadding,
            onClickApplied = { navigator.navigateToAppliedAlert() },
            onRegisterScrollToTop = onRegisterScrollToTop
        )

        appliedAlertGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onMoveToStudyScreenClick = { navigator.navigateToBoard() }
        )

        registerStudyGraph(
            contentPadding = contentPadding,
            onBackClick = { navigator.popBackStack() },
            navigateToHome = { navigator.navigateToHome() },
        )
    }
}