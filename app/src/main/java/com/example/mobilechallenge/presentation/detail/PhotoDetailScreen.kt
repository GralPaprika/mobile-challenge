package com.example.mobilechallenge.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.activity.compose.BackHandler
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import com.example.mobilechallenge.ui.theme.Accent
import com.example.mobilechallenge.ui.theme.Primary
import com.example.mobilechallenge.ui.theme.Secondary

@Composable
fun PhotoDetailScreen(
    photoId: Int,
    photoUrl: String,
    photoThumbnailUrl: String,
    photoTitle: String,
    onBackClick: () -> Unit,
    viewModel: PhotoDetailViewModel = hiltViewModel(),
) {
    val description = remember { mutableStateOf("") }
    val isLoadingDescription = remember { mutableStateOf(true) }
    val showVideoPlayer = remember { mutableStateOf(false) }

    LaunchedEffect(photoId) {
        viewModel.getPhotoDescription().collect { result ->
            isLoadingDescription.value = false
            description.value = result.getOrNull() ?: ""
        }
    }

    // Intercept back press when video player is showing
    BackHandler(enabled = showVideoPlayer.value) {
        showVideoPlayer.value = false
    }

    if (showVideoPlayer.value) {
        FullScreenVideoPlayer(
            videoUrl = photoUrl,
            onClose = { showVideoPlayer.value = false }
        )
    } else {
        PhotoDetailScreenContent(
            photoUrl = photoThumbnailUrl,
            photoTitle = photoTitle,
            photoDescription = description.value,
            isLoadingDescription = isLoadingDescription.value,
            onBackClick = onBackClick,
            onImageClick = { showVideoPlayer.value = true }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreenContent(
    photoUrl: String,
    photoTitle: String,
    photoDescription: String,
    isLoadingDescription: Boolean = false,
    onBackClick: () -> Unit,
    onImageClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = photoTitle,
                        color = Accent,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Accent
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Secondary)
                    .clickable { onImageClick() },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = photoTitle,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play video",
                    tint = Accent.copy(alpha = 0.8f),
                    modifier = Modifier
                        .background(
                            color = Primary.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(12.dp)
                )
            }

            Text(
                text = "Description",
                color = Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (isLoadingDescription) {
                SkeletonDescriptionCard()
            } else {
                Text(
                    text = photoDescription,
                    color = Accent,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoDetailScreenPreview() {
    MobileChallengeTheme {
        PhotoDetailScreenContent(
            photoUrl = "https://via.placeholder.com/600",
            photoTitle = "Sample Photo Title",
            photoDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            isLoadingDescription = false,
            onBackClick = {},
            onImageClick = {}
        )
    }
}
