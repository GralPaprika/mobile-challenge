package com.example.mobilechallenge.domain.repository

import kotlinx.coroutines.flow.Flow

interface PhotoDetailsRepository {
    fun getPhotoDescription(): Flow<Result<String>>
}