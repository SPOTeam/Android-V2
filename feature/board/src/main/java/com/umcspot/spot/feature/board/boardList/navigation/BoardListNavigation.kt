package com.umcspot.spot.feature.board.main.navigation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.feature.board.boardList.BoardListScreen
import com.umcspot.spot.feature.board.BoardViewModel
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable


fun NavController.navigateToBoardList(navOptions: NavOptions? = null) {
    navigate(BoardList, navOptions)
}

fun NavGraphBuilder.boardListGraph(
    contentPadding : PaddingValues,
    navController: NavHostController,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
) {
    composable<BoardList> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            // 🔹 NavHost 루트 그래프 기준으로 ViewModel 스코프
            navController.getBackStackEntry(navController.graph.id)
        }
        val boardViewModel: BoardViewModel = hiltViewModel(parentEntry)

        BoardListScreen(
            contentPadding = contentPadding,
            viewmodel = boardViewModel,
            onRegisterScrollToTop = onRegisterScrollToTop
        )
    }
}

@Serializable
data object BoardList : Route