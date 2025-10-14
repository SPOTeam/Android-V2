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
import com.umcspot.spot.category.navigation.navigateToCategory
import com.umcspot.spot.feature.board.navigation.Board
import com.umcspot.spot.home.navigation.Home
import com.umcspot.spot.home.navigation.navigateToHome
import com.umcspot.spot.jjim.navigation.navigateToJJim
import com.umcspot.spot.mypage.navigation.navigateToMypage
import com.umcspot.spot.mystudy.navigation.navigateToMyStudy

class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() =
            navController
                .currentBackStackEntryAsState().value?.destination

    val startDestination = Home

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
    fun showBottomBar() : Boolean {
        val dest = currentDestination ?: return false
        val inMainTabs = MainNavTab.contains { dest.hasRoute(it::class) }

        val inBoard = dest.hierarchy.any { it.hasRoute(Board::class) } // ✅ 보드 그래프 포함

        return inMainTabs || inBoard
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