package com.umcspot.spot.post.posting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.post.posting.PostingScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToPosting(navOptions: NavOptions? = null) {
    navigate(Posting, navOptions)
}

fun NavGraphBuilder.postingGraph(
    contentPadding : PaddingValues,
    onBackRequest: () -> Unit,
) {
    composable<Posting> {
        PostingScreen(
            contentPadding = contentPadding,
            onBackRequest = onBackRequest
        )
    }
}

@Serializable
data object Posting : Route