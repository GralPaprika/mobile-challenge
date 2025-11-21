package com.example.mobilechallenge.data.remote

import com.example.mobilechallenge.data.dto.AlbumDto
import com.example.mobilechallenge.data.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/albums")
    suspend fun getAlbums(): List<AlbumDto>

    @GET("/albums/{albumId}/photos")
    suspend fun getPhotosByAlbumId(@Path("albumId") albumId: Int): List<PhotoDto>
}
