package com.example.mobilechallenge.data.remote

import com.example.mobilechallenge.domain.constant.PaginationConstants
import com.example.mobilechallenge.data.dto.AlbumDto
import com.example.mobilechallenge.data.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/albums")
    suspend fun getAlbums(
        @Query("_limit") limit: Int = PaginationConstants.ALBUM_PAGE_SIZE,
        @Query("_start") start: Int = PaginationConstants.PAGINATION_START_INDEX
    ): List<AlbumDto>

    @GET("/albums/{albumId}/photos")
    suspend fun getPhotosByAlbumId(
        @Path("albumId") albumId: Int,
        @Query("_limit") limit: Int = PaginationConstants.PHOTO_PAGE_SIZE,
        @Query("_start") start: Int = PaginationConstants.PAGINATION_START_INDEX
    ): List<PhotoDto>
}
