package com.umcspot.spot.post.posting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.post.posting.PostingScreen
import com.umcspot.spot.post.posting.PostingViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToPostingNew(navOptions: NavOptions? = null) {
    navigate(Posting(postId = null), navOptions)
}

fun NavController.navigateToPostingEdit(postId: Long, navOptions: NavOptions? = null) {
    navigate(Posting(postId = postId), navOptions)
}

fun NavGraphBuilder.postingGraph(
    contentPadding : PaddingValues,
    onBackRequest: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    composable<Posting> { backStackEntry ->
        val args = backStackEntry.toRoute<Posting>()
        val postId = args.postId

        val postingViewModel: PostingViewModel = hiltViewModel()

        LaunchedEffect(postId) {
            if (postId == null) postingViewModel.clear()
            else postingViewModel.load(postId)
        }

        PostingScreen(
            contentPadding = contentPadding,
            onBackRequest = onBackRequest,
            postingViewModel = postingViewModel,
            onSubmitSuccess = onSubmitSuccess
        )
    }
}

@Serializable
data class Posting(
    val postId: Long? = null
) : Route
