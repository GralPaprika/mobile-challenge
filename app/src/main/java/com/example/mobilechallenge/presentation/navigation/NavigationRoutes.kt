package com.example.mobilechallenge.presentation.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoutes {

    @Serializable
    data object Home : NavigationRoutes()

    @Serializable
    data class PhotoDetail(
        val photoId: Int,
        val photoUrl: String,
        val photoTitle: String
    ) : NavigationRoutes()
}
