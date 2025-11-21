package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.presentation.home.model.AlbumWithPhotos
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import com.example.mobilechallenge.ui.theme.Primary

@Composable
fun HomeScreenContent(
    albumsWithPhotos: List<AlbumWithPhotos>,
    onPhotoClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Primary),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(albumsWithPhotos.take(5)) { albumWithPhotos ->
            CarouselSection(
                title = albumWithPhotos.album.title,
                photos = albumWithPhotos.photos.take(8),
                onPhotoClick = onPhotoClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreenPreview() {
    MobileChallengeTheme {
        HomeScreenContent(
            albumsWithPhotos = listOf(
                AlbumWithPhotos(
                    album = Album(
                        id = 1,
                        userId = 1,
                        title = "Album 1",
                    ),
                    photos = listOf(
                        Photo(
                            id = 1,
                            albumId = 1,
                            title = "Photo 1",
                            url = "https://via.placeholder.com/600",
                            thumbnailUrl = "https://via.placeholder.com/150"
                        ),
                        Photo(
                            id = 2,
                            albumId = 1,
                            title = "Photo 2",
                            url = "https://via.placeholder.com/600",
                            thumbnailUrl = "https://via.placeholder.com/150"
                        )
                    )
                ),
                AlbumWithPhotos(
                    album = Album(
                        id = 2,
                        userId = 1,
                        title = "Album 2",
                    ),
                    photos = listOf(
                        Photo(
                            id = 3,
                            albumId = 2,
                            title = "Photo 3",
                            url = "https://via.placeholder.com/600",
                            thumbnailUrl = "https://via.placeholder.com/150"
                        )
                    )
                )
            ),
            onPhotoClick = {}
        )
    }
}
