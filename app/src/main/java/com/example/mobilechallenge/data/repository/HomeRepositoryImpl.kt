package com.example.mobilechallenge.data.repository

import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.data.remote.ApiService
import com.example.mobilechallenge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryImpl(private val apiService: ApiService) : HomeRepository {
    override fun getAlbums(): Flow<Result<List<Album>>> = flow {
        try {
            val albums = apiService.getAlbums()
            emit(Result.success(albums))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getPhotos(): Flow<Result<List<Photo>>> = flow {
        try {
            val photos = apiService.getPhotos()
            emit(Result.success(photos))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
