package com.umcspot.spot.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

inline fun <reified T : Route, reified S : Route> NavGraphBuilder.routeNavigation(
    noinline builder: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = T::class.simpleName ?: error("Route must have simpleName"),
        startDestination = S::class.simpleName ?: error("StartDestination must have simpleName"),
        builder = builder
    )
}