package com.example.mobilechallenge.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge.presentation.home.components.shimmerBackground
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme

@Composable
fun SkeletonPhotoDetailImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .shimmerBackground()
    )
}

@Composable
fun SkeletonDescriptionCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(
            start = 8.dp,
            end = 8.dp,
            top = 8.dp,
        )
    ) {
        repeat(3) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerBackground()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkeletonPhotoDetailImagePreview() {
    MobileChallengeTheme {
        SkeletonPhotoDetailImage()
    }
}

@Preview(showBackground = true)
@Composable
fun SkeletonDescriptionCardPreview() {
    MobileChallengeTheme {
        SkeletonDescriptionCard()
    }
}
