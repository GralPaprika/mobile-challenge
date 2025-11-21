package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.presentation.home.model.AlbumWithPhotos
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import com.example.mobilechallenge.ui.theme.Primary
import kotlinx.coroutines.flow.distinctUntilChanged

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
    onLoadMorePhotosForAlbum: (Int) -> Unit = {},
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

            val isLoadingPhotosForThisAlbum = loadingPhotoIds.contains(albumWithPhotos.album.id)
            
            // Always show carousel with photos (or skeleton placeholders while loading)
            CarouselSection(
                title = albumWithPhotos.album.title,
                photos = albumWithPhotos.photos,
                onPhotoClick = onPhotoClick,
                onLoadMorePhotos = { onLoadMorePhotosForAlbum(albumWithPhotos.album.id) },
                isLoadingMorePhotos = isLoadingPhotosForThisAlbum
            )
        }

        if (isLoadingMore) {
            item {
                SkeletonAlbumCard()
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
