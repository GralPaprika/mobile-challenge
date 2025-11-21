package com.example.mobilechallenge.presentation.home.model

import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo

data class AlbumWithPhotos(
    val album: Album,
    val photos: List<Photo>,
)
