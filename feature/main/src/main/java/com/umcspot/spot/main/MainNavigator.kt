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
import com.umcspot.spot.category.navigation.navigateToCategory
import com.umcspot.spot.feature.board.boardList.navigation.BoardList
import com.umcspot.spot.feature.board.main.navigation.Board
import com.umcspot.spot.feature.board.main.navigation.navigateToBoard
import com.umcspot.spot.feature.board.post.content.navigation.POST_CONTENT_ROUTE
import com.umcspot.spot.feature.board.post.posting.navigation.Posting
import com.umcspot.spot.home.navigation.Home
import com.umcspot.spot.home.navigation.navigateToHome
import com.umcspot.spot.jjim.navigation.JJim
import com.umcspot.spot.jjim.navigation.navigateToJJim
import com.umcspot.spot.mypage.main.navigation.MyPage
import com.umcspot.spot.mypage.main.navigation.navigateToMyPage
import com.umcspot.spot.mypage.participating.navigation.ParticipatingStudy
import com.umcspot.spot.mypage.recruiting.navigation.MyRecruitingStudy
import com.umcspot.spot.mypage.waiting.navigation.WaitingStudy
import com.umcspot.spot.signup.navigation.CheckList
import com.umcspot.spot.signup.navigation.Landing
import com.umcspot.spot.signup.navigation.Saving
import com.umcspot.spot.signup.navigation.SignUp
import com.umcspot.spot.signup.navigation.navigateToCheckList
import com.umcspot.spot.signup.navigation.navigateToSaving
import com.umcspot.spot.signup.navigation.navigateToSignUp
import com.umcspot.spot.study.detail.navigation.navigateToStudyDetail
import com.umcspot.spot.study.my.navigation.navigateToMyStudy
import com.umcspot.spot.study.preferCategory.navigation.PreferCategory
import com.umcspot.spot.study.preferCategory.navigation.PreferCategoryFilter
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

    val startDestination = Landing

    val currentTab: MainNavTab?
        @Composable get() =
            MainNavTab.find { tab ->
                currentDestination?.hasRoute(tab::class) == true
            }

    fun navigate(tab: MainNavTab) {
        val navOptions =
            navOptions {
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
    fun isInLanding(): Boolean = inAnyGraph(Landing::class, Saving::class)

    @Composable
    fun showBackTopBar(): Boolean = inAnyGraph(Alert::class, RecruitingFilter::class, PreferLocationFilter::class, PreferCategoryFilter::class,
        SignUp::class, CheckList::class, Posting::class, BoardList::class, JJim::class, MyPage::class,
        ParticipatingStudy::class, MyRecruitingStudy::class, WaitingStudy::class
    ) || inAnyGraphRoutes(POST_CONTENT_ROUTE)

    @Composable
    fun showToTopFab(): Boolean = inAnyGraph(Alert::class, Recruiting::class,
        PreferLocation::class, PreferCategory::class, BoardList::class, JJim::class, ParticipatingStudy::class, MyRecruitingStudy::class, WaitingStudy::class)

    @Composable
    fun showMultipleFab(): Boolean = inAnyGraph(Home::class, BoardList::class)

    @Composable
    fun showBottomBar(): Boolean {
        val dest = currentDestination ?: return false
        val inMainTabs = MainNavTab.contains { dest.hasRoute(it::class) }

        val showBottomBar = dest.isInAnyGraph(
            Board::class, Recruiting::class
        )

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