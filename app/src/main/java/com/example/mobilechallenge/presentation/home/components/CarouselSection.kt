package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import com.example.mobilechallenge.ui.theme.Accent

@Composable
fun CarouselSection(
    title: String,
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("carousel_section")
    ) {
        Text(
            text = title,
            color = Accent,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(photos) { photo ->
                PhotoCard(photo = photo, onPhotoClick = onPhotoClick)
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
