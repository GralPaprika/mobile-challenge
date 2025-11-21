package com.example.mobilechallenge.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.presentation.home.components.ErrorScreen
import com.example.mobilechallenge.presentation.home.components.LoadingScreen
import com.example.mobilechallenge.presentation.home.components.HomeScreenContent
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPhotoClick: (Photo) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    when {
        uiState.isLoading -> LoadingScreen()
        uiState.error != null -> ErrorScreen(uiState.error)
        else -> HomeScreenContent(
            albumsWithPhotos = uiState.albumsWithPhotos,
            isLoadingMore = uiState.isLoadingMore,
            loadingPhotoIds = uiState.loadingPhotoIds,
            hasMoreData = uiState.hasMoreData,
            onPhotoClick = onPhotoClick,
            onLoadMoreRequested = { viewModel.loadNextPage() },
            onPhotoLoadRequested = { albumId -> viewModel.loadPhotosForAlbum(albumId) },
            onLoadMorePhotosForAlbum = { albumId -> viewModel.loadPhotosForAlbum(albumId) },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MobileChallengeTheme {
        LoadingScreen()
    }
}
