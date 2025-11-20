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
import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import com.example.mobilechallenge.ui.theme.Primary

@Composable
fun SuccessScreen(
    albums: List<Album>,
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Primary),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(albums.take(5)) { album ->
            CarouselSection(
                title = album.title,
                photos = photos.filter { it.albumId == album.id }.take(8),
                onPhotoClick = onPhotoClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreenPreview() {
    MobileChallengeTheme {
        SuccessScreen(
            albums = listOf(
                Album(id = 1, userId = 1, title = "Album 1"),
                Album(id = 2, userId = 1, title = "Album 2")
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
                ),
                Photo(
                    id = 3,
                    albumId = 2,
                    title = "Photo 3",
                    url = "https://via.placeholder.com/600",
                    thumbnailUrl = "https://via.placeholder.com/150"
                )
            ),
            onPhotoClick = {}
        )
    }
}
