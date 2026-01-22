package com.umcspot.spot.main

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.umcspot.spot.alert.navigation.alertGraph
import com.umcspot.spot.category.navigation.categoryFilterGraph
import com.umcspot.spot.category.navigation.categoryGraph
import com.umcspot.spot.category.navigation.navigateToCategoryFilter
import com.umcspot.spot.feature.board.boardList.navigation.boardListGraph
import com.umcspot.spot.feature.board.boardList.navigation.navigateToBoardList
import com.umcspot.spot.feature.board.main.navigation.boardGraph
import com.umcspot.spot.feature.board.post.content.navigation.navigateToPostContent
import com.umcspot.spot.feature.board.post.content.navigation.postContentGraph
import com.umcspot.spot.feature.board.post.posting.navigation.navigateToPostingEdit
import com.umcspot.spot.feature.board.post.posting.navigation.postingGraph
import com.umcspot.spot.home.navigation.homeGraph
import com.umcspot.spot.jjim.navigation.jjimGraph
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.mypage.main.navigation.mypageGraph
import com.umcspot.spot.mypage.participating.navigation.navigateToParticipatingStudy
import com.umcspot.spot.mypage.participating.navigation.participatingGraph
import com.umcspot.spot.mypage.recruiting.navigation.myRecruitingStudyGraph
import com.umcspot.spot.mypage.recruiting.navigation.navigateToMyRecruitingStudy
import com.umcspot.spot.mypage.waiting.navigation.navigateToWaitingStudy
import com.umcspot.spot.mypage.waiting.navigation.waitingStudyGraph
import com.umcspot.spot.signup.navigation.signupGraph
import com.umcspot.spot.study.detail.navigation.navigateToStudyDetail
import com.umcspot.spot.study.detail.navigation.studyDetailGraph
import com.umcspot.spot.study.my.navigation.myStudyGraph
import com.umcspot.spot.study.preferCategory.navigation.navigateToPreferCategoryStudy
import com.umcspot.spot.study.preferCategory.navigation.navigateToPreferCategoryStudyFilter
import com.umcspot.spot.study.preferCategory.navigation.preferCategoryStudyFilterGraph
import com.umcspot.spot.study.preferCategory.navigation.preferCategoryStudyGraph
import com.umcspot.spot.study.preferLocation.navigation.preferLocationStudyFilterGraph
import com.umcspot.spot.study.preferLocation.navigation.preferLocationStudyGraph
import com.umcspot.spot.study.recruiting.navigation.recruitingStudyFilterGraph
import com.umcspot.spot.study.recruiting.navigation.recruitingStudyGraph
import com.umcspot.spot.study.register.navigation.RegisterStudy
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

        signupGraph(
            navigateToSignUp = { navigator.navigateToSignUp() },
            navigateToCheckList = { navigator.navigateToCheckList() },
            navigateToSaving = { navigator.navigateToSaving() },
            navigateToHome = { navigator.navigateToHome(clearStackNavOptions) },
            contentPadding = contentPadding,
        )

        homeGraph(
            contentPadding = contentPadding,
            onQuickMenuClick = { type ->
                when (type) {
                    QuickMenuType.BOARD -> navigator.navigateToBoard()
                    QuickMenuType.REGION -> navigator.navigateToPreferLocationStudy()
                    QuickMenuType.INTERESTS -> navigator.navController.navigateToPreferCategoryStudy()
                    QuickMenuType.RECRUITING -> navigator.navigateToRecruitingStudy()
                }
            },
            onPopularClick = { navigator.navigateToBoard() },
            onPopularPostClick = { navigator.navController.navigateToPostContent(it) },
            onStudyClick = {  },
            onStudyMoreClick = {  }
        )
        categoryGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = { navigator.navController.navigateToStudyDetail(it) },
            onFilterClick = { navigator.navController.navigateToCategoryFilter() },
        )

        categoryFilterGraph(
            navController = navigator.navController,
            contentPadding = contentPadding,
            onAcceptFilterClick = { navigator.popBackStack() }
        )

        myStudyGraph()

        jjimGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onMoveToStudyClick = { navigator.navigateToRecruitingStudy() },
            onItemClick = { navigator.navigateToStudyDetail(it) }
        )

        mypageGraph(
            contentPadding = contentPadding,
            onParticipatingClick = { navigator.navController.navigateToParticipatingStudy() },
            onMyRecruitingClick = { navigator.navController.navigateToMyRecruitingStudy() },
            onMyAppliedClick = { navigator.navController.navigateToWaitingStudy() },
            onEditInterestClick = { /*navigator.navigateToCheckList*/ },
            onEditInterestLocationClick =  {  }
        )

        participatingGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onStudyClick = { navigator.navigateToStudyDetail(it) },
            moveToRecruitingStudy = { navigator.navigateToRecruitingStudy() }
        )

        myRecruitingStudyGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onStudyClick = { navigator.navigateToStudyDetail(it) },
            moveToMakeStudy = {  },
            moveToCheckApplied = { }
        )

        waitingStudyGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onStudyClick = { navigator.navigateToStudyDetail(it) },
            moveToRecruitingStudy = { navigator.navigateToRecruitingStudy() },
        )

        recruitingStudyGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = { },
            onFilterClick = { navigator.navigateToRecruitingStudyFilter() },
        )

        recruitingStudyFilterGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onAcceptFilterClick = { navigator.popBackStack() }
        )

        preferLocationStudyGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = { },
            onFilterClick = { navigator.navigateToPreferLocationStudyFilter() },
        )

        preferLocationStudyFilterGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onAcceptFilterClick = { navigator.popBackStack() }
        )

        preferCategoryStudyGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = { },
            onFilterClick = { navigator.navController.navigateToPreferCategoryStudyFilter() },
        )

        preferCategoryStudyFilterGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onAcceptFilterClick = { navigator.popBackStack() }
        )

        boardGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onMoveToBoardList = { navigator.navController.navigateToBoardList() },
            onMoveToPostContent = { navigator.navController.navigateToPostContent(it) }
        )

        boardListGraph(
            contentPadding = contentPadding,
            navController = navigator.navController,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onPostClick = { navigator.navController.navigateToPostContent(it) }
        )

        postingGraph(
            contentPadding = contentPadding,
            onBackRequest = onBackRequest,
            onSubmitSuccess = {navigator.navController.popBackStack()}
        )

        postContentGraph(
            contentPadding = contentPadding,
            onDeleteClick = {
                val popped = navigator.navController.popBackStack()
                Log.d("NAV", "popBackStack popped=$popped")
            },
            onEditClick = { navigator.navController.navigateToPostingEdit(it) }
        )

        alertGraph(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onClickAlert = { navigator.navigateToStudyDetail(it) }
        )

        registerStudyGraph(
            contentPadding = contentPadding,
            onBackClick = { navigator.navigateToHome(clearStackNavOptions) },
            navigateToStudyDetail = { studyId ->
                navigator.navigateToStudyDetail(
                    studyId = studyId,
                    navOptions = navOptions {
                        popUpTo<RegisterStudy> { inclusive = true }
                    }
                )
            }
        )

        studyDetailGraph(
            contentPadding = contentPadding,
            onBackClick = { navigator.navigateToMyStudy(clearStackNavOptions) }
        )
    }
}