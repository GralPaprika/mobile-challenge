package com.example.mobilechallenge.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.presentation.home.components.ErrorScreen
import com.example.mobilechallenge.presentation.home.components.LoadingScreen
import com.example.mobilechallenge.presentation.home.components.SuccessScreen
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
        else -> SuccessScreen(
            albums = uiState.albums,
            photos = uiState.photos,
            onPhotoClick = onPhotoClick,
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
