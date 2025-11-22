package com.example.mobilechallenge.data.mapper

import com.example.mobilechallenge.data.dto.AlbumDto
import com.example.mobilechallenge.data.dto.PhotoDto
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo

object DataMapper {
    fun albumDtoToDomain(dto: AlbumDto): Album {
        return Album(
            id = dto.id,
            userId = dto.userId,
            title = dto.title,
        )
    }

    fun photoDtoToDomain(dto: PhotoDto): Photo {
        return Photo(
            id = dto.id,
            albumId = dto.albumId,
            title = dto.title,
            url = dto.url,
            thumbnailUrl = "https://picsum.photos/300/200",
        )
    }

    fun albumDtosToDomain(dtos: List<AlbumDto>): List<Album> {
        return dtos.map { albumDtoToDomain(it) }
    }

    fun photoDtosToDomain(dtos: List<PhotoDto>): List<Photo> {
        return dtos.map { photoDtoToDomain(it) }
    }
}
