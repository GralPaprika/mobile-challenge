package com.example.mobilechallenge.domain.usecase

import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

interface GetPhotosUseCase {
    operator fun invoke(albumId: Int): Flow<Result<List<Photo>>>
}

class GetPhotosUseCaseImpl(private val repository: HomeRepository) : GetPhotosUseCase {
    override fun invoke(albumId: Int): Flow<Result<List<Photo>>> = repository.getPhotosByAlbumId(albumId)
}
