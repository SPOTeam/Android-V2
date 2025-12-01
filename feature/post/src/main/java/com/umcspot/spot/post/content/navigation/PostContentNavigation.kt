package com.umcspot.spot.post.content.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.feature.board.BoardViewModel
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.post.content.PostContentScreen
import kotlinx.serialization.Serializable


fun NavController.navigateToPostContent(navOptions: NavOptions? = null) {
    navigate(PostContent, navOptions)
}

fun NavGraphBuilder.postContentGraph(
    contentPadding : PaddingValues,
    navController : NavController
) {
    composable<PostContent> {backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            // 🔹 NavHost 루트 그래프 기준으로 ViewModel 스코프
            navController.getBackStackEntry(navController.graph.id)
        }
        val boardViewModel: BoardViewModel = hiltViewModel(parentEntry)
        PostContentScreen(
            contentPadding = contentPadding,
            boardViewModel = boardViewModel
        )
    }
}

@Serializable
data object PostContent : Route