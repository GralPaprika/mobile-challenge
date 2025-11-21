package com.example.mobilechallenge.data.dto

import com.google.gson.annotations.SerializedName

data class AlbumDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("title")
    val title: String,
)
