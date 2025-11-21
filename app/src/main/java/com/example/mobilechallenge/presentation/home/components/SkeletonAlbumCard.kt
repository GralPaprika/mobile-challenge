package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme

@Composable
fun SkeletonAlbumCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("skeleton_album_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(26.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerBackground()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            repeat(3) {
                SkeletonPhotoCard()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkeletonAlbumCardPreview() {
    MobileChallengeTheme {
        SkeletonAlbumCard()
    }
}
