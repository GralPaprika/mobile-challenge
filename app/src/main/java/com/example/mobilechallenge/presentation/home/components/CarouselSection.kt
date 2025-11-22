package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.ui.theme.Accent
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import kotlinx.coroutines.flow.distinctUntilChanged

// Load more carousel photos when within 3 items of end
private const val CAROUSEL_SCROLL_TRIGGER = 3

// Show 3 skeleton cards during carousel pagination
private const val SKELETON_CAROUSEL_PAGINATION_COUNT = 3

@Composable
fun CarouselSection(
    modifier: Modifier = Modifier,
    title: String,
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit,
    onLoadMorePhotos: () -> Unit = {},
    isLoadingMorePhotos: Boolean = false,
) {
    val listState = rememberLazyListState()

    // Detect when user scrolls near the end of carousel
    LaunchedEffect(listState, isLoadingMorePhotos, photos.size) {
        snapshotFlow {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = photos.size + if (isLoadingMorePhotos) SKELETON_CAROUSEL_PAGINATION_COUNT else 0 // Account for skeleton items
            lastVisibleIndex to totalItems
        }
            .distinctUntilChanged()
            .collect { (lastVisibleIndex, totalItems) ->
                if (lastVisibleIndex >= totalItems - CAROUSEL_SCROLL_TRIGGER && totalItems > 0 && !isLoadingMorePhotos) {
                    onLoadMorePhotos()
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("carousel_section")
    ) {
        Text(
            text = title,
            color = Accent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier.fillMaxWidth(),
            state = listState
        ) {
            items(photos) { photo ->
                PhotoCard(photo = photo, onPhotoClick = onPhotoClick)
            }
            
            // Show skeleton cards while loading
            if (isLoadingMorePhotos) {
                items(SKELETON_CAROUSEL_PAGINATION_COUNT) {
                    SkeletonPhotoCard()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselSectionPreview() {
    MobileChallengeTheme {
        CarouselSection(
            title = "Album Title",
            photos = listOf(
                Photo(
                    id = 1,
                    albumId = 1,
                    title = "Photo 1",
                    url = "https://via.placeholder.com/600",
                    thumbnailUrl = "https://via.placeholder.com/150",
                ),
                Photo(
                    id = 2,
                    albumId = 1,
                    title = "Photo 2",
                    url = "https://via.placeholder.com/600",
                    thumbnailUrl = "https://via.placeholder.com/150",
                ),
                Photo(
                    id = 3,
                    albumId = 1,
                    title = "Photo 3",
                    url = "https://via.placeholder.com/600",
                    thumbnailUrl = "https://via.placeholder.com/150",
                )
            ),
            onPhotoClick = {}
        )
    }
}
