package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme

@Composable
fun SkeletonPhotoCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(140.dp)
            .height(140.dp)
            .padding(end = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .shimmerBackground()
    )
}

@Preview(showBackground = true)
@Composable
fun SkeletonPhotoCardPreview() {
    MobileChallengeTheme {
        SkeletonPhotoCard()
    }
}
