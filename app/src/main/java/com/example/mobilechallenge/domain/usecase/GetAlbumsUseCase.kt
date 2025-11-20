package com.example.mobilechallenge.domain.usecase

import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

interface GetAlbumsUseCase {
    operator fun invoke(): Flow<Result<List<Album>>>
}

class GetAlbumsUseCaseImpl(private val repository: HomeRepository) : GetAlbumsUseCase {
    override fun invoke(): Flow<Result<List<Album>>> = repository.getAlbums()
}
