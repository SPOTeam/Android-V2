package com.umcspot.spot.feature.board.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.feature.board.BoardListScreen
import com.umcspot.spot.feature.board.BoardScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable


fun NavController.navigateToBoardList(navOptions: NavOptions? = null) {
    navigate(BoardList, navOptions)
}

fun NavGraphBuilder.boardListGraph(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,

) {
    composable<BoardList> {
        BoardListScreen(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop
        )
    }
}

@Serializable
data object BoardList : Route