package com.example.mobilechallenge.domain.usecase

import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

interface GetAlbumsUseCase {
    operator fun invoke(limit: Int = 5, start: Int = 0): Flow<Result<List<Album>>>
}

class GetAlbumsUseCaseImpl(private val repository: HomeRepository) : GetAlbumsUseCase {
    override fun invoke(limit: Int, start: Int): Flow<Result<List<Album>>> = repository.getAlbums(limit = limit, start = start)
}
