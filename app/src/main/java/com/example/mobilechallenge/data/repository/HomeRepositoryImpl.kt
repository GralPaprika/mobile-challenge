package com.example.mobilechallenge.data.repository

import com.example.mobilechallenge.data.mapper.DataMapper
import com.example.mobilechallenge.data.remote.ApiService
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryImpl(private val apiService: ApiService) : HomeRepository {
    override fun getAlbums(limit: Int, start: Int): Flow<Result<List<Album>>> = flow {
        try {
            val dtos = apiService.getAlbums(limit = limit, start = start)
            val albums = DataMapper.albumDtosToDomain(dtos)
            emit(Result.success(albums))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getPhotosByAlbumId(albumId: Int, limit: Int, start: Int): Flow<Result<List<Photo>>> = flow {
        try {
            val dtos = apiService.getPhotosByAlbumId(albumId, limit, start)
            val photos = DataMapper.photoDtosToDomain(dtos)
            emit(Result.success(photos))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
