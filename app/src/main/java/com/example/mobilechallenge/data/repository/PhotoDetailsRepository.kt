package com.example.mobilechallenge.data.repository

import com.example.mobilechallenge.data.remote.LoremApiService

interface PhotoDetailsRepository {
    suspend fun getPhotoDescription(): String
}

class PhotoDetailsRepositoryImpl(
    private val loremApiService: LoremApiService
) : PhotoDetailsRepository {
    override suspend fun getPhotoDescription(): String {
        return loremApiService.getLoremText()
    }
}
