package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.snapshotFlow
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.presentation.home.model.AlbumWithPhotos
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import com.example.mobilechallenge.ui.theme.Primary

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    albumsWithPhotos: List<AlbumWithPhotos>,
    isLoadingMore: Boolean = false,
    loadingPhotoIds: Set<Int> = emptySet(),
    hasMoreData: Boolean = true,
    onPhotoClick: (Photo) -> Unit,
    onLoadMoreRequested: () -> Unit = {},
    onPhotoLoadRequested: (Int) -> Unit = {},
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, isLoadingMore, hasMoreData) {
        snapshotFlow { 
            val totalItems = albumsWithPhotos.size
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisibleIndex to totalItems
        }
            .distinctUntilChanged()
            .collect { (lastVisibleIndex, totalItems) ->
                if (lastVisibleIndex >= totalItems - 3 && !isLoadingMore && totalItems > 0 && hasMoreData) {
                    onLoadMoreRequested()
                }
            }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Primary),
        contentPadding = PaddingValues(vertical = 16.dp),
        state = listState
    ) {
        items(albumsWithPhotos) { albumWithPhotos ->
            LaunchedEffect(albumWithPhotos.album.id) {
                if (albumWithPhotos.photos.isEmpty() && albumWithPhotos.album.id !in loadingPhotoIds) {
                    onPhotoLoadRequested(albumWithPhotos.album.id)
                }
            }

            if (loadingPhotoIds.contains(albumWithPhotos.album.id)) {
                SkeletonAlbumCard()
            } else {
                CarouselSection(
                    title = albumWithPhotos.album.title,
                    photos = albumWithPhotos.photos.take(8),
                    onPhotoClick = onPhotoClick
                )
            }
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    SkeletonAlbumCard()
                }
            }
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
