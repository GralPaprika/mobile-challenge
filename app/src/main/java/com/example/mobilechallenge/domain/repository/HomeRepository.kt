package com.example.mobilechallenge.domain.repository

import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAlbums(): Flow<Result<List<Album>>>
    fun getPhotosByAlbumId(albumId: Int): Flow<Result<List<Photo>>>
}
