package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import com.example.mobilechallenge.ui.theme.Secondary
import com.example.mobilechallenge.ui.theme.Accent

@Composable
fun PhotoCard(
    modifier: Modifier = Modifier,
    photo: Photo,
    onPhotoClick: (Photo) -> Unit,
) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(180.dp)
            .padding(end = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Secondary)
            .clickable { onPhotoClick(photo) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                .data(photo.thumbnailUrl)
                .crossfade(true)
                .build(),
            contentDescription = photo.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Text(
            text = photo.title.take(20),
            color = Accent,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoCardPreview() {
    MobileChallengeTheme {
        PhotoCard(
            photo = Photo(
                id = 1,
                albumId = 1,
                title = "Sample photo title",
                url = "https://via.placeholder.com/600",
                thumbnailUrl = "https://via.placeholder.com/150",
            ),
            onPhotoClick = {}
        )
    }
}
