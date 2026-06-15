package com.umcspot.spot.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.umcspot.spot.mypage.cancelMemberShip.CancelMemberShipScreen
import com.umcspot.spot.mypage.edit.editInterestRegion.EditInterestRegionScreen
import com.umcspot.spot.mypage.edit.editInterestStudy.EditInterestStudyScreen
import com.umcspot.spot.mypage.edit.editStudy.EditStudyRoute
import com.umcspot.spot.mypage.leave.LeaveStudyRoute
import com.umcspot.spot.mypage.MyPageRoute
import com.umcspot.spot.mypage.participating.ParticipatingRoute
import com.umcspot.spot.mypage.recruiting.RecruitingStudyRoute
import com.umcspot.spot.mypage.recruiting.application.RecruitingStudyRequestScreen
import com.umcspot.spot.mypage.waiting.WaitingStudyRoute
import com.umcspot.spot.navigation.MainTabRoute
import com.umcspot.spot.navigation.routeNavigation
import kotlinx.serialization.Serializable

@Serializable
sealed class MyPageGraph : MainTabRoute {
    @Serializable
    data object MyPage : MyPageGraph()
    @Serializable
    data object ParticipatingStudy : MyPageGraph()
    @Serializable
    data object MyRecruitingStudy : MyPageGraph()
    @Serializable
    data object WaitingStudy : MyPageGraph()
    @Serializable
    data object EditInterest : MyPageGraph()
    @Serializable
    data object EditRegion : MyPageGraph()
    @Serializable
    data object CancelMemberShip : MyPageGraph()
    @Serializable
    data class EditStudy(val studyId: Long) : MyPageGraph()
    @Serializable
    data class StudyApplications(val studyId: Long) : MyPageGraph()
    @Serializable
    data class LeaveStudy(
        val studyId: Long,
        val isOwner: Boolean,
        val studyName: String,
        val studyDescription: String,
        val profileImageUrl: String?
    ) : MyPageGraph()
}

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) =
    navigate(MyPageGraph.MyPage, navOptions)

fun NavController.navigateToParticipatingStudy(navOptions: NavOptions? = null) =
    navigate(MyPageGraph.ParticipatingStudy, navOptions)

fun NavController.navigateToMyRecruitingStudy(navOptions: NavOptions? = null) =
    navigate(MyPageGraph.MyRecruitingStudy, navOptions)

fun NavController.navigateToWaitingStudy(navOptions: NavOptions? = null) =
    navigate(MyPageGraph.WaitingStudy, navOptions)

fun NavController.navigateToEditInterestStudy(navOptions: NavOptions? = null) =
    navigate(MyPageGraph.EditInterest, navOptions)

fun NavController.navigateToEditInterestRegion(navOptions: NavOptions? = null) =
    navigate(MyPageGraph.EditRegion, navOptions)

fun NavController.navigateToCancelMembership(navOptions: NavOptions? = null) =
    navigate(MyPageGraph.CancelMemberShip, navOptions)

fun NavController.navigateToEditStudy(studyId: Long, navOptions: NavOptions? = null) =
    navigate(MyPageGraph.EditStudy(studyId), navOptions)

fun NavController.navigateToStudyApplications(studyId: Long, navOptions: NavOptions? = null) =
    navigate(MyPageGraph.StudyApplications(studyId), navOptions)

fun NavController.navigateToLeaveStudy(
    studyId: Long,
    isOwner: Boolean,
    studyName: String,
    studyDescription: String,
    profileImageUrl: String?,
    navOptions: NavOptions? = null
) =
    navigate(
        MyPageGraph.LeaveStudy(studyId, isOwner, studyName, studyDescription, profileImageUrl),
        navOptions
    )

fun NavGraphBuilder.myPageGraph(
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onStudyClick: (Long) -> Unit,
    onLogoutClick: () -> Unit,
    navigateToStudyDetail: (Long) -> Unit,
    moveToRecruitingStudy: () -> Unit,
    moveToPreferCategoryStudy: () -> Unit,
    moveToPreferLocationStudy: () -> Unit,
    successCancelMemberShip: () -> Unit,
    navigateUp: () -> Unit,
    navigateToParticipatingStudy: () -> Unit,
    navigateToMyRecruitingStudy: () -> Unit,
    navigateToWaitingStudy: () -> Unit,
    navigateToMakeStudy: () -> Unit,
    navigateToEditInterestStudy: () -> Unit,
    navigateToEditInterestRegion: () -> Unit,
    navigateToCancelMembership: () -> Unit,
    navigateToEditStudy: (Long) -> Unit,
    navigateToLeaveStudy: (Long, Boolean, String, String, String?) -> Unit,
    navigateToStudyApplications: (Long) -> Unit
) {
    routeNavigation<MyPageGraph, MyPageGraph.MyPage> {
        composable<MyPageGraph.MyPage> {
            MyPageRoute(
                contentPadding = contentPadding,
                onParticipatingClick = navigateToParticipatingStudy,
                onMyRecruitingClick = navigateToMyRecruitingStudy,
                onMyAppliedClick = navigateToWaitingStudy,
                onEditInterestClick = navigateToEditInterestStudy,
                onEditInterestLocationClick = navigateToEditInterestRegion,
                onCancelMemberShipClick = navigateToCancelMembership,
                onLogoutClick = onLogoutClick
            )
        }
        composable<MyPageGraph.ParticipatingStudy> {
            ParticipatingRoute(
                contentPadding = contentPadding,
                onBackClick = navigateUp,
                onRegisterScrollToTop = onRegisterScrollToTop,
                onStudyClick = onStudyClick,
                moveToRecruitingStudy = moveToRecruitingStudy,
                navigateToEditStudy = navigateToEditStudy,
                navigateToLeaveStudy = navigateToLeaveStudy
            )
        }
        composable<MyPageGraph.MyRecruitingStudy> {
            RecruitingStudyRoute(
                contentPadding = contentPadding,
                onBackClick = navigateUp,
                onRegisterScrollToTop = onRegisterScrollToTop,
                onStudyClick = onStudyClick,
                moveToMakeStudy = navigateToMakeStudy,
                moveToCheckApplied = navigateToStudyApplications
            )
        }
        composable<MyPageGraph.WaitingStudy> {
            WaitingStudyRoute(
                contentPadding = contentPadding,
                onBackClick = navigateUp,
                onRegisterScrollToTop = onRegisterScrollToTop,
                onStudyClick = onStudyClick,
                moveToRecruitingStudy = moveToRecruitingStudy
            )
        }
        composable<MyPageGraph.EditInterest> {
            EditInterestStudyScreen(
                contentPadding = contentPadding,
                moveToMyInterestStudy = moveToPreferCategoryStudy,
                moveToMyPage = navigateUp
            )
        }
        composable<MyPageGraph.EditRegion> {
            EditInterestRegionScreen(
                contentPadding = contentPadding,
                moveToMyInterestStudy = moveToPreferLocationStudy,
                moveToMyPage = navigateUp
            )
        }
        composable<MyPageGraph.CancelMemberShip> {
            CancelMemberShipScreen(
                contentPadding = contentPadding,
                successCancelMemberShip = successCancelMemberShip,
                moveToParticipatingStudy = navigateToParticipatingStudy
            )
        }

        composable<MyPageGraph.StudyApplications> { backStackEntry ->
            val args = backStackEntry.toRoute<MyPageGraph.StudyApplications>()
            RecruitingStudyRequestScreen(
                contentPadding = contentPadding,
                studyId = args.studyId,
                onRegisterScrollToTop = onRegisterScrollToTop
            )
        }

        composable<MyPageGraph.EditStudy> { backStackEntry ->
            val args = backStackEntry.toRoute<MyPageGraph.EditStudy>()
            EditStudyRoute(
                studyId = args.studyId,
                contentPadding = contentPadding,
                onBackClick = navigateUp,
                navigateToStudyDetail = navigateToStudyDetail
            )
        }
        composable<MyPageGraph.LeaveStudy> { backStackEntry ->
            val args = backStackEntry.toRoute<MyPageGraph.LeaveStudy>()
            LeaveStudyRoute(
                studyId = args.studyId,
                isOwner = args.isOwner,
                studyName = args.studyName,
                studyDescription = args.studyDescription,
                profileImageUrl = args.profileImageUrl,
                contentPadding = contentPadding,
                onBackClick = navigateUp,
                onLeaveSuccess = navigateUp
            )
        }
    }
}