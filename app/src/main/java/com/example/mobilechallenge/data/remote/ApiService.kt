package com.example.mobilechallenge.data.remote

import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import retrofit2.http.GET

interface ApiService {
    @GET("/albums")
    suspend fun getAlbums(): List<Album>

    @GET("/photos")
    suspend fun getPhotos(): List<Photo>
}
