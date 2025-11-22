package com.example.mobilechallenge.domain.usecase

import com.example.mobilechallenge.data.remote.LoremApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GetPhotoDescriptionUseCase {
    operator fun invoke(): Flow<Result<String>>
}

class GetPhotoDescriptionUseCaseImpl(private val loremApiService: LoremApiService) : GetPhotoDescriptionUseCase {
    override fun invoke(): Flow<Result<String>> = flow {
        try {
            val response = loremApiService.getLoremText()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
