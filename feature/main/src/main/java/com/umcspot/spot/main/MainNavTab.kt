package com.umcspot.spot.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.umcspot.spot.category.navigation.Category
import com.umcspot.spot.designsystem.R.drawable.home_default
import com.umcspot.spot.designsystem.R.drawable.category_default
import com.umcspot.spot.designsystem.R.drawable.study_default
import com.umcspot.spot.designsystem.R.drawable.like_default
import com.umcspot.spot.designsystem.R.drawable.mypage_default
import com.umcspot.spot.home.navigation.Home
import com.umcspot.spot.jjim.navigation.JJim
import com.umcspot.spot.main.R.string.ic_home_desc
import com.umcspot.spot.main.R.string.ic_category_desc
import com.umcspot.spot.main.R.string.ic_mystudy_desc
import com.umcspot.spot.main.R.string.ic_jjim_desc
import com.umcspot.spot.main.R.string.ic_mypage_desc
import com.umcspot.spot.mypage.navigation.Mypage
import com.umcspot.spot.mystudy.navigation.MyStudy
import com.umcspot.spot.navigation.MainTabRoute
import com.umcspot.spot.navigation.Route

enum class MainNavTab(
    @DrawableRes val icon: Int,
    @StringRes val contentDescription: Int,
    val route: MainTabRoute
) {
    HOME(
        icon = home_default,
        contentDescription = ic_home_desc,
        route = Home
    ),
    CATEGORY(
        icon = category_default,
        contentDescription = ic_category_desc,
        route = Category
    ),
    MYSTUDY(
        icon = study_default,
        contentDescription = ic_mystudy_desc,
        route = MyStudy
    ),
    JJIM(
        icon = like_default,
        contentDescription = ic_jjim_desc,
        route = JJim
    ),
    MYPAGE(
        icon = mypage_default,
        contentDescription = ic_mypage_desc,
        route = Mypage
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainNavTab? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}