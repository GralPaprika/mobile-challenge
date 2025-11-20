package com.example.mobilechallenge.domain.usecase

import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface LoadHomeScreenUseCase {
    operator fun invoke(): Flow<Pair<Result<List<Album>>, Result<List<Photo>>>>
}

class LoadHomeScreenUseCaseImpl(
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val getPhotosUseCase: GetPhotosUseCase
) : LoadHomeScreenUseCase {
    override fun invoke(): Flow<Pair<Result<List<Album>>, Result<List<Photo>>>> =
        combine(
            getAlbumsUseCase.invoke(),
            getPhotosUseCase.invoke()
        ) { albumsResult, photosResult ->
            Pair(albumsResult, photosResult)
        }
}
