package com.umcspot.spot.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.umcspot.spot.alert.navigation.Alert
import com.umcspot.spot.alert.navigation.navigateToAlert
import com.umcspot.spot.category.navigation.CategoryGraph
import com.umcspot.spot.category.navigation.navigateToCategory
import com.umcspot.spot.category.navigation.navigateToCategoryFilter
import com.umcspot.spot.feature.board.boardList.navigation.BoardList
import com.umcspot.spot.feature.board.main.navigation.Board
import com.umcspot.spot.feature.board.main.navigation.navigateToBoard
import com.umcspot.spot.feature.board.post.content.navigation.POST_CONTENT_ROUTE
import com.umcspot.spot.feature.board.post.posting.navigation.Posting
import com.umcspot.spot.home.navigation.Home
import com.umcspot.spot.home.navigation.navigateToHome
import com.umcspot.spot.jjim.navigation.JJim
import com.umcspot.spot.jjim.navigation.navigateToJJim
import com.umcspot.spot.mypage.navigation.MyPageGraph
import com.umcspot.spot.mypage.navigation.navigateToCancelMembership
import com.umcspot.spot.mypage.navigation.navigateToEditInterestRegion
import com.umcspot.spot.mypage.navigation.navigateToEditInterestStudy
import com.umcspot.spot.mypage.navigation.navigateToEditStudy
import com.umcspot.spot.mypage.navigation.navigateToLeaveStudy
import com.umcspot.spot.mypage.navigation.navigateToMyPage
import com.umcspot.spot.mypage.navigation.navigateToMyRecruitingStudy
import com.umcspot.spot.mypage.navigation.navigateToParticipatingStudy
import com.umcspot.spot.mypage.navigation.navigateToStudyApplications
import com.umcspot.spot.mypage.navigation.navigateToWaitingStudy
import com.umcspot.spot.signup.navigation.CheckList
import com.umcspot.spot.signup.navigation.Landing
import com.umcspot.spot.signup.navigation.Saving
import com.umcspot.spot.signup.navigation.SignUp
import com.umcspot.spot.signup.navigation.Splash
import com.umcspot.spot.signup.navigation.navigateToCheckList
import com.umcspot.spot.signup.navigation.navigateToLanding
import com.umcspot.spot.signup.navigation.navigateToSaving
import com.umcspot.spot.signup.navigation.navigateToSignUp
import com.umcspot.spot.study.detail.navigation.StudyDetail
import com.umcspot.spot.study.detail.navigation.navigateToStudyDetail
import com.umcspot.spot.study.detail.navigation.navigateToStudyMemoirPost
import com.umcspot.spot.study.my.navigation.MyStudy
import com.umcspot.spot.study.my.navigation.navigateToMyStudy
import com.umcspot.spot.study.preferCategory.navigation.PreferCategory
import com.umcspot.spot.study.preferCategory.navigation.PreferCategoryFilter
import com.umcspot.spot.study.preferCategory.navigation.navigateToPreferCategoryStudyFilter
import com.umcspot.spot.study.preferLocation.navigation.PreferLocation
import com.umcspot.spot.study.preferLocation.navigation.PreferLocationFilter
import com.umcspot.spot.study.preferLocation.navigation.navigateToPreferLocationStudy
import com.umcspot.spot.study.preferLocation.navigation.navigateToPreferLocationStudyFilter
import com.umcspot.spot.study.recruiting.navigation.Recruiting
import com.umcspot.spot.study.recruiting.navigation.RecruitingFilter
import com.umcspot.spot.study.recruiting.navigation.navigateToRecruitingStudy
import com.umcspot.spot.study.recruiting.navigation.navigateToRecruitingStudyFilter
import com.umcspot.spot.study.register.navigation.navigateToRegisterStudy
import kotlin.reflect.KClass

class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() =
            navController
                .currentBackStackEntryAsState().value?.destination

    val startDestination = Splash

    val currentTab: MainNavTab?
        @Composable get() =
            MainNavTab.find { tab ->
                currentDestination?.hasRoute(tab::class) == true
            }

    fun navigate(tab: MainNavTab) {
        val navOptions = navOptions {
            navController.currentDestination?.route?.let {
                popUpTo(it) {
                    inclusive = true
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
        when (tab) {
            MainNavTab.HOME -> navController.navigateToHome(navOptions)
            MainNavTab.CATEGORY -> navController.navigateToCategory(navOptions)
            MainNavTab.MYSTUDY -> navController.navigateToMyStudy(navOptions)
            MainNavTab.JJIM -> navController.navigateToJJim(navOptions)
            MainNavTab.MYPAGE -> navController.navigateToMyPage(navOptions)
        }
    }

    @Composable
    private fun inAnyGraph(vararg graphs: KClass<*>): Boolean {
        val dest = currentDestination ?: return false
        return dest.hierarchy.any { h -> graphs.any { g -> h.hasRoute(g) } }
    }

    @Composable
    private fun inAnyGraphRoutes(vararg routes: String): Boolean {
        val dest = currentDestination ?: return false
        return dest.hierarchy.any { h -> routes.any { r -> h.routeMatches(r) } }
    }

    @Composable
    fun isInLanding(): Boolean = inAnyGraph(Splash::class, Landing::class, Saving::class)

    @Composable
    fun showBackTopBar(): Boolean = inAnyGraph(
        Alert::class,
        RecruitingFilter::class,
        PreferLocationFilter::class,
        PreferCategoryFilter::class,
        SignUp::class,
        CheckList::class,
        Posting::class,
        BoardList::class,
        JJim::class,
        MyStudy::class,
        MyPageGraph.StudyApplications::class
    ) || inAnyGraphRoutes(POST_CONTENT_ROUTE)|| (currentDestination?.route?.contains(POST_CONTENT_ROUTE) == true)

    @Composable
    fun showToTopFab(): Boolean = inAnyGraph(
        Alert::class,
        Recruiting::class,
        PreferLocation::class,
        PreferCategory::class,
        BoardList::class,
        JJim::class,
        MyStudy::class,
        CategoryGraph.Category::class,
        MyPageGraph.ParticipatingStudy::class,
        MyPageGraph.MyRecruitingStudy::class,
        MyPageGraph.WaitingStudy::class,
        MyPageGraph.StudyApplications::class
    )

    @Composable
    fun showMultipleFab(): Boolean = inAnyGraph(Home::class, BoardList::class, StudyDetail::class)

    @Composable
    fun showBottomBar(): Boolean {
        val dest = currentDestination ?: return false
        val inMainTabs = MainNavTab.contains { dest.hasRoute(it::class) }
        val showBottomBar = dest.isInAnyGraph(Board::class, Recruiting::class)
        return inMainTabs || showBottomBar
    }

    private fun NavDestination.isInAnyGraph(vararg graphs: KClass<*>): Boolean {
        return hierarchy.any { h -> graphs.any { k -> h.hasRoute(k) } }
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigateToLanding(navOptions: NavOptions? = null) {
        navController.navigateToLanding(navOptions)
    }

    fun navigateToSignUp(navOptions: NavOptions? = null) {
        navController.navigateToSignUp(navOptions)
    }

    fun navigateToCheckList(navOptions: NavOptions? = null) {
        navController.navigateToCheckList(navOptions)
    }

    fun navigateToSaving(navOptions: NavOptions? = null) {
        navController.navigateToSaving(navOptions)
    }

    fun navigateToHome(navOptions: NavOptions? = null) {
        navController.navigateToHome(navOptions)
    }

    fun navigateToCategory(navOptions: NavOptions? = null) {
        navController.navigateToCategory(navOptions)
    }

    fun navigateToCategoryFilter(navOptions: NavOptions? = null) {
        navController.navigateToCategoryFilter(navOptions)
    }

    fun navigateToMyStudy(navOptions: NavOptions? = null) {
        navController.navigateToMyStudy(navOptions)
    }

    fun navigateToRegisterStudy(navOptions: NavOptions? = null) {
        navController.navigateToRegisterStudy(navOptions)
    }

    fun navigateToBoard(navOptions: NavOptions? = null) {
        navController.navigateToBoard(navOptions)
    }

    fun navigateToPreferLocationStudy(navOptions: NavOptions? = null) {
        navController.navigateToPreferLocationStudy(navOptions)
    }

    fun navigateToRecruitingStudy(navOptions: NavOptions? = null) {
        navController.navigateToRecruitingStudy(navOptions)
    }

    fun navigateToRecruitingStudyFilter(navOptions: NavOptions? = null) {
        navController.navigateToRecruitingStudyFilter(navOptions)
    }

    fun navigateToPreferLocationStudyFilter(navOptions: NavOptions? = null) {
        navController.navigateToPreferLocationStudyFilter(navOptions)
    }

    fun navigateToAlert(navOptions: NavOptions? = null) {
        navController.navigateToAlert(navOptions)
    }

    fun navigateToStudyDetail(studyId: Long, navOptions: NavOptions? = null) {
        navController.navigateToStudyDetail(studyId, navOptions)
    }

    fun navigateToStudyMemoirPost(studyId: Long) {
        navController.navigateToStudyMemoirPost(studyId)
    }

    fun navigateToMyPage(navOptions: NavOptions? = null) {
        navController.navigateToMyPage(navOptions)
    }


    fun navigateToParticipatingStudy(navOptions: NavOptions? = null) {
        navController.navigateToParticipatingStudy(navOptions)
    }

    fun navigateToMyRecruitingStudy(navOptions: NavOptions? = null) {
        navController.navigateToMyRecruitingStudy(navOptions)
    }

    fun navigateToWaitingStudy(navOptions: NavOptions? = null) {
        navController.navigateToWaitingStudy(navOptions)
    }

    fun navigateToEditInterestStudy(navOptions: NavOptions? = null) {
        navController.navigateToEditInterestStudy(navOptions)
    }

    fun navigateToEditInterestRegion(navOptions: NavOptions? = null) {
        navController.navigateToEditInterestRegion(navOptions)
    }

    fun navigateToCancelMembership(navOptions: NavOptions? = null) {
        navController.navigateToCancelMembership(navOptions)
    }

    fun navigateToEditStudy(studyId: Long, navOptions: NavOptions? = null) {
        navController.navigateToEditStudy(studyId, navOptions)
    }

    fun navigateToLeaveStudy(
        studyId: Long,
        isOwner: Boolean,
        studyName: String,
        studyDescription: String,
        profileImageUrl: String?,
        navOptions: NavOptions? = null
    ) {
        navController.navigateToLeaveStudy(studyId, isOwner, studyName, studyDescription, profileImageUrl, navOptions)
    }

    fun navigateToStudyApplications(studyId: Long) = navController.navigateToStudyApplications(studyId)

    fun navigateToPreferCategoryStudyFilter(navOptions: NavOptions? = null) {
        navController.navigateToPreferCategoryStudyFilter(navOptions)
    }


    fun navigateToHomeAfterLogin() {
        val navOptions = navOptions {
            popUpTo(Landing) { inclusive = true }
        }
        navController.navigateToHome(navOptions)
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}

fun NavDestination.routeMatches(pattern: String): Boolean {
    val actual = route ?: return false
    return if (pattern.contains("{")) {
        actual.startsWith(pattern.substringBefore("{"))
    } else actual == pattern
}