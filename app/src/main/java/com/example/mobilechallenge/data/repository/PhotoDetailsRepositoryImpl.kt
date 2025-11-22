package com.example.mobilechallenge.data.repository

import com.example.mobilechallenge.data.remote.LoremApiService
import com.example.mobilechallenge.domain.repository.PhotoDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotoDetailsRepositoryImpl(
    private val loremApiService: LoremApiService
) : PhotoDetailsRepository {
    override fun getPhotoDescription(): Flow<Result<String>> = flow {
        try {
            val response = loremApiService.getLoremText()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
