package com.umcspot.spot.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.umcspot.spot.alert.navigation.Alert
import com.umcspot.spot.alert.navigation.navigateToAlert
import com.umcspot.spot.category.navigation.CategoryGraph
import com.umcspot.spot.designsystem.component.FloatingMultipleButton
import com.umcspot.spot.designsystem.component.FloatingToUpButton
import com.umcspot.spot.designsystem.component.appBar.AppBarHome
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.modal.RejectDialog
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.feature.board.boardList.navigation.BoardList
import com.umcspot.spot.feature.board.post.content.navigation.POST_CONTENT_ROUTE
import com.umcspot.spot.feature.board.post.posting.navigation.Posting
import com.umcspot.spot.feature.board.post.posting.navigation.navigateToPostingNew
import com.umcspot.spot.home.navigation.Home
import com.umcspot.spot.home.navigation.navigateToHome
import com.umcspot.spot.jjim.navigation.JJim
import com.umcspot.spot.main.component.MainBottomBar
import com.umcspot.spot.mypage.navigation.MyPageGraph
import com.umcspot.spot.signup.navigation.CheckList
import com.umcspot.spot.signup.navigation.SignUp
import com.umcspot.spot.study.detail.model.StudyDetailTab
import com.umcspot.spot.study.detail.navigation.StudyAttendance
import com.umcspot.spot.study.detail.navigation.StudyDetail
import com.umcspot.spot.study.detail.navigation.StudyMemoirPost
import com.umcspot.spot.study.my.navigation.MyStudy
import com.umcspot.spot.study.preferCategory.navigation.PreferCategoryFilter
import com.umcspot.spot.study.preferLocation.navigation.PreferLocationFilter
import com.umcspot.spot.study.recruiting.navigation.RecruitingFilter
import com.umcspot.spot.study.register.navigation.RegisterStudy
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = navigator.navController
    val backStackEntry by navController.currentBackStackEntryAsState()
    val dest = backStackEntry?.destination

    var scrollToTop by remember { mutableStateOf<(() -> Unit)?>(null) }
    var showBackRequestDialog by remember { mutableStateOf(false) }
    var currentStudyDetailTab by rememberSaveable { mutableStateOf(StudyDetailTab.HOME) }

    val hasUnreadAlert by mainViewModel.hasUnreadAlert.collectAsStateWithLifecycle()
    val isHome = dest?.hasRoute(Home::class) == true

    LaunchedEffect(isHome) {
        if (isHome) mainViewModel.loadUnreadAlerts()
    }

    Scaffold(
        topBar = {
            if (!navigator.isInLanding()) {
                val isFullPage = dest?.hasRoute(RegisterStudy::class) == true ||
                        dest?.hasRoute(StudyDetail::class) == true ||
                        dest?.hasRoute(StudyMemoirPost::class) == true ||
                        dest?.hasRoute(StudyAttendance::class) == true ||
                        dest?.hasRoute(MyPageGraph.EditStudy::class) == true ||
                        dest?.hasRoute(MyPageGraph.LeaveStudy::class) == true ||
                        dest?.hasRoute(JJim::class) == true ||
                        dest?.hasRoute(MyStudy::class) == true ||
                        dest?.hasRoute(MyPageGraph.EditInterest::class) == true ||
                        dest?.hasRoute(MyPageGraph.EditRegion::class) == true ||
                        dest?.hasRoute(CategoryGraph.CategoryFilter::class) == true ||
                        dest?.hasRoute(MyPageGraph.MyPage::class) == true ||
                        dest?.hasRoute(MyPageGraph.ParticipatingStudy::class) == true ||
                        dest?.hasRoute(MyPageGraph.MyRecruitingStudy::class) == true ||
                        dest?.hasRoute(MyPageGraph.WaitingStudy::class) == true
                if (isFullPage) {
                } else if (navigator.showBackTopBar()) {
                    val title = when {
                        dest?.hasRoute(Alert::class) == true -> "알림"
                        dest?.hasRoute(RecruitingFilter::class) == true -> "모집중인 스터디"
                        dest?.hasRoute(PreferLocationFilter::class) == true -> "내 지역 스터디"
                        dest?.hasRoute(PreferCategoryFilter::class) == true -> "내 관심사 스터디"
                        dest?.hasRoute(SignUp::class) == true -> "회원가입"
                        dest?.hasRoute(CheckList::class) == true -> "체크리스트"
                        dest?.hasRoute(Posting::class) == true -> "글쓰기"
                        dest?.hasRoute(BoardList::class) == true -> "스터디 파트너들의 이야기"
                        dest?.hasRoute(JJim::class) == true -> "찜한 스터디"
                        dest?.hasRoute(MyStudy::class) == true -> "내 스터디"
                        dest?.hasRoute(MyPageGraph.StudyApplications::class) == true -> "신청 확인"
                        dest?.hasRoute(MyPageGraph.CancelMemberShip::class) == true -> "회원 탈퇴"
                        dest?.routeMatches(POST_CONTENT_ROUTE) == true -> "스터디 파트너들의 이야기"
                        else -> ""
                    }
                    BackTopBar(
                        title = title,
                        onBackClick = {
                            if (dest?.hasRoute(Posting::class) == true) {
                                showBackRequestDialog = true
                            } else {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.statusBarsPadding()
                    )
                } else {
                    AppBarHome(
                        hasAlert = hasUnreadAlert,
                        onAlertClick = { navController.navigateToAlert() },
                        onLogoClick = {
                            navController.navigateToHome(
                                navOptions = navOptions {
                                    popUpTo<Home> { inclusive = true }
                                }
                            )
                        },
                        modifier = Modifier.statusBarsPadding()
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FabStack(
                showToTop = navigator.showToTopFab(),
                onClickToTop = { scrollToTop?.invoke() },
                showMultiple = navigator.showMultipleFab() && (
                        dest?.hasRoute(Home::class) == true ||
                                dest?.hasRoute(BoardList::class) == true ||
                                (dest?.hasRoute(StudyDetail::class) == true && currentStudyDetailTab == StudyDetailTab.MEMOIR)
                        ),
                onClickMultiple = {
                    when {
                        dest?.hasRoute(BoardList::class) == true -> {
                            navigator.navController.navigateToPostingNew()
                        }
                        dest?.hasRoute(Home::class) == true -> {
                            navigator.navigateToRegisterStudy()
                        }
                        dest?.hasRoute(StudyDetail::class) == true -> {
                            val studyId = backStackEntry?.toRoute<StudyDetail>()?.studyId
                            if (studyId != null) {
                                if (currentStudyDetailTab == StudyDetailTab.MEMOIR) {
                                    navigator.navigateToStudyMemoirPost(studyId)
                                } else if (currentStudyDetailTab == StudyDetailTab.BOARD) {
                                    navigator.navigateToStudyBoardPost(studyId)
                                }
                            }
                        }
                    }
                },
                spacing = 12.dp,
            )
        },
        bottomBar = {
            if (!navigator.isInLanding()) {
                MainBottomBar(
                    visible = navigator.showBottomBar(),
                    tabs = MainNavTab.entries.toImmutableList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = navigator::navigate,
                )
            }
        },
        modifier = Modifier
            .background(SpotTheme.colors.white)
            .fillMaxSize()
    ) { innerPadding ->
        MainNavHost(
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            contentPadding = innerPadding,
            onRegisterScrollToTop = { handler -> scrollToTop = handler },
            onBackRequest = { showBackRequestDialog = true },
            onStudyTabChanged = { tab -> currentStudyDetailTab = tab },
            currentStudyDetailTab = currentStudyDetailTab,
        )
    }

    RejectDialog(
        visible = showBackRequestDialog,
        modalTitle = "나가시겠어요?",
        modalDes = "지금 나가면, 쓰던 글은 저장되지 않아요",
        okButtonText = "네",
        noButtonText = "아니요",
        onDismiss = { showBackRequestDialog = false },
        onClick = {
            showBackRequestDialog = false
            navController.popBackStack()
        },
        onCancel = { showBackRequestDialog = false }
    )
}

@Composable
private fun FabStack(
    showToTop: Boolean,
    onClickToTop: () -> Unit,
    showMultiple: Boolean,
    onClickMultiple: () -> Unit,
    spacing: Dp = 12.dp,
) {
    Box(modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            if(showToTop) FloatingToUpButton(onClick = onClickToTop)
            if(showMultiple) FloatingMultipleButton(onClick = onClickMultiple)
        }
    }
}
