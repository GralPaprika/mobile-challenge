package com.example.mobilechallenge.domain.usecase

import app.cash.turbine.test
import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoadHomeScreenUseCaseTest {

    private lateinit var getAlbumsUseCase: GetAlbumsUseCase
    private lateinit var getPhotosUseCase: GetPhotosUseCase
    private lateinit var useCase: LoadHomeScreenUseCase

    @Before
    fun setUp() {
        getAlbumsUseCase = mock()
        getPhotosUseCase = mock()
        useCase = LoadHomeScreenUseCaseImpl(getAlbumsUseCase, getPhotosUseCase)
    }

    @Test
    fun `invoke combines both albums and photos results`() = runTest {
        val testAlbums = listOf(Album(id = 1, userId = 1, title = "Album 1"))
        val testPhotos = listOf(Photo(id = 1, albumId = 1, title = "Photo 1", url = "url", thumbnailUrl = "thumb"))

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.success(testAlbums))
        })
        whenever(getPhotosUseCase.invoke()).thenReturn(flow {
            emit(Result.success(testPhotos))
        })

        useCase.invoke().test {
            val (albumsResult, photosResult) = awaitItem()
            assertTrue(albumsResult.isSuccess)
            assertTrue(photosResult.isSuccess)
            assertEquals(testAlbums, albumsResult.getOrNull())
            assertEquals(testPhotos, photosResult.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke handles album error`() = runTest {
        val testException = Exception("Album error")
        val testPhotos = listOf(Photo(id = 1, albumId = 1, title = "Photo 1", url = "url", thumbnailUrl = "thumb"))

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.failure(testException))
        })
        whenever(getPhotosUseCase.invoke()).thenReturn(flow {
            emit(Result.success(testPhotos))
        })

        useCase.invoke().test {
            val (albumsResult, photosResult) = awaitItem()
            assertTrue(albumsResult.isFailure)
            assertTrue(photosResult.isSuccess)
            assertEquals("Album error", albumsResult.exceptionOrNull()?.message)
            awaitComplete()
        }
    }

    @Test
    fun `invoke handles photo error`() = runTest {
        val testAlbums = listOf(Album(id = 1, userId = 1, title = "Album 1"))
        val testException = Exception("Photo error")

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.success(testAlbums))
        })
        whenever(getPhotosUseCase.invoke()).thenReturn(flow {
            emit(Result.failure(testException))
        })

        useCase.invoke().test {
            val (albumsResult, photosResult) = awaitItem()
            assertTrue(albumsResult.isSuccess)
            assertTrue(photosResult.isFailure)
            assertEquals("Photo error", photosResult.exceptionOrNull()?.message)
            awaitComplete()
        }
    }
}
