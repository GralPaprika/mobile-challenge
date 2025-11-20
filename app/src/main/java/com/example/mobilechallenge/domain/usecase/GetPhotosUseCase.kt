package com.example.mobilechallenge.domain.usecase

import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

interface GetPhotosUseCase {
    operator fun invoke(): Flow<Result<List<Photo>>>
}

class GetPhotosUseCaseImpl(private val repository: HomeRepository) : GetPhotosUseCase {
    override fun invoke(): Flow<Result<List<Photo>>> = repository.getPhotos()
}
