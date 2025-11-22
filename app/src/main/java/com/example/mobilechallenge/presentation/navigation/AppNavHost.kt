package com.example.mobilechallenge.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mobilechallenge.presentation.detail.PhotoDetailScreen
import com.example.mobilechallenge.presentation.home.HomeScreen
import com.example.mobilechallenge.presentation.home.HomeViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Home
    ) {
        composable<NavigationRoutes.Home> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onPhotoClick = { photo ->
                    navController.navigate(
                        NavigationRoutes.PhotoDetail(
                            id = photo.id,
                            url = photo.url,
                            title = photo.title,
                            thumbnailUrl = photo.thumbnailUrl,
                        )
                    )
                }
            )
        }

        composable<NavigationRoutes.PhotoDetail> { backStackEntry ->
            val photoDetail = backStackEntry.toRoute<NavigationRoutes.PhotoDetail>()
            
            PhotoDetailScreen(
                photoId = photoDetail.id,
                photoUrl = photoDetail.url,
                photoThumbnailUrl = photoDetail.thumbnailUrl,
                photoTitle = photoDetail.title,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
