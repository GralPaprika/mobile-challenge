package com.example.mobilechallenge.domain.repository

import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAlbums(limit: Int = 5, start: Int = 0): Flow<Result<List<Album>>>
    fun getPhotosByAlbumId(albumId: Int, limit: Int = 10, start: Int = 0): Flow<Result<List<Photo>>>
}
