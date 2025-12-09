package com.umcspot.spot.post.content.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.umcspot.spot.feature.board.boardList.BoardListViewModel
import com.umcspot.spot.feature.board.main.BoardViewModel
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.post.content.PostContentScreen
import kotlinx.serialization.Serializable


fun NavController.navigateToPostContent(postId: Long) {
    navigate("post/$postId")
}

fun NavGraphBuilder.postContentGraph(
    contentPadding : PaddingValues,
    navController : NavController
) {
    composable(
        route = POST_CONTENT_ROUTE,
        arguments = listOf(
            navArgument("postId") { type = NavType.LongType }
        )
    ) { backStackEntry ->
        val postId = backStackEntry.arguments?.getLong("postId") ?: return@composable

        PostContentScreen(
            contentPadding = contentPadding,
            postId = postId
        )
    }
}

@Serializable
const val POST_CONTENT_ROUTE = "post/{postId}"
