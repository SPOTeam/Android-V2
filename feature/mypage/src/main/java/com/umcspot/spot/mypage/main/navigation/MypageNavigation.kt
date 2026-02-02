package com.umcspot.spot.mypage.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.mypage.main.MyPageScreen
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) {
    navigate(MyPage, navOptions)
}

fun NavGraphBuilder.mypageGraph(
    contentPadding : PaddingValues,
    onParticipatingClick : () -> Unit,
    onMyRecruitingClick : () -> Unit,
    onMyAppliedClick : () -> Unit,
    onEditInterestClick : () -> Unit,
    onEditInterestLocationClick : () -> Unit,
    onCancelMemberShipClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    composable<MyPage> {
        MyPageScreen(
            contentPadding = contentPadding,
            onParticipatingClick = onParticipatingClick,
            onMyRecruitingClick = onMyRecruitingClick,
            onMyAppliedClick = onMyAppliedClick,
            onEditInterestClick = onEditInterestClick,
            onEditInterestLocationClick = onEditInterestLocationClick,
            onCancelMemberShipClick = onCancelMemberShipClick,
            onLogoutClick = onLogoutClick
        )
    }
}

@Serializable
data object MyPage : MainTabRoute