package com.example.mobilechallenge.domain.usecase

import com.example.mobilechallenge.domain.repository.PhotoDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GetPhotoDescriptionUseCase {
    operator fun invoke(): Flow<Result<String>>
}

class GetPhotoDescriptionUseCaseImpl(private val photoDetailsRepository: PhotoDetailsRepository) : GetPhotoDescriptionUseCase {
    override fun invoke(): Flow<Result<String>> = photoDetailsRepository.getPhotoDescription()
}
