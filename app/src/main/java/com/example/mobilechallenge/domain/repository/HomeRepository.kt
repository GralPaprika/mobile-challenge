package com.example.mobilechallenge.domain.repository

import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAlbums(): Flow<Result<List<Album>>>
    fun getPhotos(): Flow<Result<List<Photo>>>
}
