package com.example.mobilechallenge.presentation.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoutes {

    @Serializable
    data object Home : NavigationRoutes()

    @Serializable
    data class PhotoDetail(
        val id: Int = 0,
        val thumbnailUrl: String,
        val title: String,
    ) : NavigationRoutes()
}
