package com.example.mobilechallenge.data.remote

import com.example.mobilechallenge.data.dto.AlbumDto
import com.example.mobilechallenge.data.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/albums")
    suspend fun getAlbums(
        @Query("_limit") limit: Int = 5,
        @Query("_start") start: Int = 0
    ): List<AlbumDto>

    @GET("/albums/{albumId}/photos")
    suspend fun getPhotosByAlbumId(
        @Path("albumId") albumId: Int,
        @Query("_limit") limit: Int = 10,
        @Query("_start") start: Int = 0
    ): List<PhotoDto>
}
