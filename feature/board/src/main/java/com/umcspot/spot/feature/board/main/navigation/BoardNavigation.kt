package com.umcspot.spot.feature.board.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.feature.board.BoardScreen
import com.umcspot.spot.feature.board.main.BoardViewModel
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable


fun NavController.navigateToBoard(navOptions: NavOptions? = null) {
    navigate(Board, navOptions)
}

fun NavGraphBuilder.boardGraph(
    contentPadding : PaddingValues,
    navController: NavHostController,
    onMoveToBoardList : () -> Unit,
) {
    composable<Board> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            // 🔹 NavHost 루트 그래프 기준으로 ViewModel 스코프
            navController.getBackStackEntry(navController.graph.id)
        }
        val boardViewModel: BoardViewModel = hiltViewModel(parentEntry)

        BoardScreen(
            contentPadding = contentPadding,
            viewmodel = boardViewModel,
            onMoveToBoardList = onMoveToBoardList,
        )
    }

}

@Serializable
data object Board : Route