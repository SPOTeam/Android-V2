package com.umcspot.spot.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.umcspot.spot.alert.navigation.Alert
import com.umcspot.spot.alert.navigation.AppliedAlert
import com.umcspot.spot.category.navigation.navigateToCategory
import com.umcspot.spot.feature.board.BoardListScreen
import com.umcspot.spot.feature.board.navigation.Board
import com.umcspot.spot.feature.board.navigation.BoardList
import com.umcspot.spot.feature.board.navigation.Posting
import com.umcspot.spot.home.navigation.navigateToHome
import com.umcspot.spot.jjim.navigation.navigateToJJim
import com.umcspot.spot.landing.Landing
import com.umcspot.spot.mypage.navigation.navigateToMypage
import com.umcspot.spot.study.recruiting.navigation.Recruiting
import com.umcspot.spot.study.my.navigation.navigateToMyStudy
import com.umcspot.spot.study.preferLocation.PreferLocationStudyScreen
import com.umcspot.spot.study.preferLocation.navigation.PreferLocation
import com.umcspot.spot.study.recruiting.navigation.RecruitingFilter
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
            MainNavTab.MYPAGE -> navController.navigateToMypage(navOptions)
        }
    }

    @Composable
    private fun inAnyGraph(vararg graphs: KClass<*>): Boolean {
        val dest = currentDestination ?: return false
        return dest.hierarchy.any { h -> graphs.any { g -> h.hasRoute(g) } }
    }

    @Composable
    fun isInLanding(): Boolean = inAnyGraph(Landing::class)

    /** 상단 뒤로가기 TopBar 노출 조건 */
    @Composable
    fun showBackTopBar(): Boolean = inAnyGraph(Alert::class, AppliedAlert::class, RecruitingFilter::class,
        BoardList::class, Posting::class
     )

    /** 스크롤-투-탑 FAB 노출 조건 */
    @Composable
    fun showToTopFab(): Boolean = inAnyGraph(Alert::class, AppliedAlert::class, Recruiting::class,
        PreferLocation::class, BoardList::class
    )

    /** 멀티 FAB(게시판 등) 노출 조건 */
    @Composable
    fun showMultipleFab(): Boolean = inAnyGraph(Board::class, BoardList::class
    )

    @Composable
    fun showBottomBar() : Boolean {
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
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}