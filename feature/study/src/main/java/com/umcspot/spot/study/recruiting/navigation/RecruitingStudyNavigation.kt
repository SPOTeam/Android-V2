package com.umcspot.spot.study.recruiting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.study.recruiting.RecruitingStudyScreen
import com.umcspot.spot.study.model.StudyResult
import kotlinx.serialization.Serializable

fun NavController.navigateToRecruitingStudy(navOptions: NavOptions? = null) {
    navigate(Recruiting, navOptions)
}

fun NavGraphBuilder.recruitingStudyGraph(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onFilterClick : () -> Unit,
    onItemClick : (StudyResult) -> Unit
) {
    composable<Recruiting> { backStackEntry ->
        // ✅ 부모 그래프(= "recruiting_graph")의 BackStackEntry를 ViewModelStoreOwner로 지정
        val parentEntry = remember(backStackEntry) {
            backStackEntry.navController.getBackStackEntry(RecruitingGraph.ROUTE)
        }
        CompositionLocalProvider(LocalViewModelStoreOwner provides parentEntry) {
            RecruitingStudyScreen(
                contentPadding = contentPadding,
                onRegisterScrollToTop = onRegisterScrollToTop,
                onFilterClick = onFilterClick,
                onItemClick = onItemClick
            )
        }
    }
}

internal object RecruitingGraph {
    const val ROUTE = "recruiting_graph" // 외부에 Route로 노출하지 않음(문자열만)
}

@Serializable
data object Recruiting : Route