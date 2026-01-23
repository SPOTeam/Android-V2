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
import com.umcspot.spot.alert.navigation.navigateToAlert
import com.umcspot.spot.designsystem.component.FloatingMultipleButton
import com.umcspot.spot.designsystem.component.FloatingToUpButton
import com.umcspot.spot.designsystem.component.appBar.AppBarHome
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.modal.RejectDialog
import com.umcspot.spot.feature.board.boardList.navigation.BoardList
import com.umcspot.spot.feature.board.post.content.navigation.POST_CONTENT_ROUTE
import com.umcspot.spot.feature.board.post.posting.navigation.Posting
import com.umcspot.spot.feature.board.post.posting.navigation.navigateToPostingNew
import com.umcspot.spot.home.navigation.Home
import com.umcspot.spot.jjim.navigation.JJim
import com.umcspot.spot.main.component.MainBottomBar
import com.umcspot.spot.mypage.cancelMemberShip.CancelMemberShipScreen
import com.umcspot.spot.mypage.cancelMemberShip.navigation.CancelMemberShip
import com.umcspot.spot.mypage.editInterestStudy.navigation.EditInterest
import com.umcspot.spot.mypage.main.navigation.MyPage
import com.umcspot.spot.mypage.participating.navigation.ParticipatingStudy
import com.umcspot.spot.mypage.recruiting.navigation.MyRecruitingStudy
import com.umcspot.spot.mypage.waiting.navigation.WaitingStudy
import com.umcspot.spot.signup.navigation.CheckList
import com.umcspot.spot.signup.navigation.SignUp
import com.umcspot.spot.study.detail.navigation.StudyDetail
import com.umcspot.spot.study.preferCategory.navigation.PreferCategoryFilter
import com.umcspot.spot.study.preferLocation.navigation.PreferLocationFilter
import com.umcspot.spot.study.recruiting.navigation.RecruitingFilter
import com.umcspot.spot.study.register.navigation.RegisterStudy
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    val navController = navigator.navController
    val backStackEntry by navController.currentBackStackEntryAsState()
    val dest = backStackEntry?.destination
    var scrollToTop by remember { mutableStateOf<(() -> Unit)?>(null) }

    var showBackRequestDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (!navigator.isInLanding()) {
                val isRegisterOrDetail = dest?.hasRoute(RegisterStudy::class) == true ||
                        dest?.hasRoute(StudyDetail::class) == true

                if (isRegisterOrDetail) {
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
                        dest?.hasRoute(MyPage::class) == true -> "마이페이지"
                        dest?.hasRoute(ParticipatingStudy::class) == true -> "참여 중인 스터디"
                        dest?.hasRoute(MyRecruitingStudy::class) == true -> "모집 중인 스터디"
                        dest?.hasRoute(WaitingStudy::class) == true -> "대기 중인 스터디"
                        dest?.hasRoute(EditInterest::class) == true -> "관심 분야"
                        dest?.hasRoute(CancelMemberShip::class) == true -> "회원 탈퇴"
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
                onClickMultiple = {
                    when {
                        dest?.hasRoute(BoardList::class) == true -> {
                            navigator.navController.navigateToPostingNew()
                        }
                        dest?.hasRoute(Home::class) == true -> {
                            navigator.navigateToRegisterStudy()
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
            .background(Color.White)
            .fillMaxSize()
    ) { innerPadding ->
        MainNavHost(
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            contentPadding =  innerPadding,
            onRegisterScrollToTop = { handler -> scrollToTop = handler },
            onBackRequest = { showBackRequestDialog = true },
        )
    }

    RejectDialog(
        visible = showBackRequestDialog,
        modalTitle = "나가시겠어요?",
        modalDes = "지금 나가면, 쓰던 글은 저장되지 않아요",
        okButtonText = "네",
        noButtonText = "아니요",
        onDismiss = {
            showBackRequestDialog = false
        },
        onClick = {
            showBackRequestDialog = false
            navController.popBackStack()
        },
        onCancel = {
            showBackRequestDialog = false
        }
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
    Box(
        modifier = Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            if(showToTop) {
                FloatingToUpButton(onClick = onClickToTop)
            }
            if(showMultiple) {
                FloatingMultipleButton(onClick = onClickMultiple)
            }
        }
    }
}
