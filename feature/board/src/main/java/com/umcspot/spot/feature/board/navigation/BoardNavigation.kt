package com.umcspot.spot.feature.board.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.feature.board.BoardScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable


fun NavController.navigateToBoard(navOptions: NavOptions? = null) {
    navigate(Board, navOptions)
}

fun NavGraphBuilder.boardGraph(
    contentPadding : PaddingValues
) {
    composable<Board> {
        BoardScreen(contentPadding = contentPadding)
    }
}

@Serializable
data object Board : Route