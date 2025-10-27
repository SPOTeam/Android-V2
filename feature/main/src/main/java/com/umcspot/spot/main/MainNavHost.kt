package com.umcspot.spot.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.umcspot.spot.alert.navigation.alertGraph
import com.umcspot.spot.alert.navigation.appliedAlertGraph
import com.umcspot.spot.alert.navigation.navigateToAppliedAlert
import com.umcspot.spot.category.navigation.categoryGraph
import com.umcspot.spot.checkList.navigation.CheckList
import com.umcspot.spot.checkList.navigation.checkListGraph
import com.umcspot.spot.checkList.navigation.navigateToCheckList
import com.umcspot.spot.feature.board.navigation.boardGraph
import com.umcspot.spot.feature.board.navigation.navigateToBoard
import com.umcspot.spot.home.navigation.homeGraph
import com.umcspot.spot.home.navigation.navigateToHome
import com.umcspot.spot.jjim.navigation.jjimGraph
import com.umcspot.spot.landing.navigation.landingGraph
import com.umcspot.spot.landing.navigation.navigateToSaving
import com.umcspot.spot.landing.navigation.savingGraph
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.mypage.navigation.mypageGraph
import com.umcspot.spot.signup.navigation.SignUp
import com.umcspot.spot.signup.navigation.navigateToSignUp
import com.umcspot.spot.signup.navigation.signupGraph
import com.umcspot.spot.study.my.navigation.myStudyGraph
import com.umcspot.spot.study.recruiting.navigation.navigateToRecruitingStudy
import com.umcspot.spot.study.recruiting.navigation.navigateToRecruitingStudyFilter
import com.umcspot.spot.study.recruiting.navigation.recruitingStudyFilterGraph
import com.umcspot.spot.study.recruiting.navigation.recruitingStudyGraph

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
            onKakaoClick = { navigator.navController.navigateToSignUp() },
            onNaverClick = { navigator.navController.navigateToSignUp() }
        )

        signupGraph(
            contentPadding = contentPadding,
            onNextClick = { navigator.navController.navigateToCheckList() }
        )

        checkListGraph(
            contentPadding = contentPadding,
            onNextClick = { navigator.navController.navigateToSaving() }
        )

        savingGraph(
            contentPadding = contentPadding,
            onFinished = { navigator.navController.navigateToHome(
                navOptions {
                    popUpTo(navigator.navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = false
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
            onFilterClick = { navigator.navController.navigateToRecruitingStudyFilter() },
            onItemClick = {}
        )

        recruitingStudyFilterGraph(
            contentPadding = contentPadding,
            onAcceptFilterClick = { navigator.navController.popBackStack() }
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