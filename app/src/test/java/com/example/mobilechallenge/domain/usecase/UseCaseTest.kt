package com.example.mobilechallenge.domain.usecase

import app.cash.turbine.test
import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.data.repository.HomeRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAlbumsUseCaseTest {

    private lateinit var repository: HomeRepository
    private lateinit var useCase: GetAlbumsUseCase

    @Before
    fun setUp() {
        repository = mock()
        useCase = GetAlbumsUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns success result with albums`() = runTest {
        val testAlbums = listOf(
            Album(id = 1, userId = 1, title = "Album 1"),
            Album(id = 2, userId = 1, title = "Album 2")
        )

        whenever(repository.getAlbums()).thenReturn(flow {
            emit(Result.success(testAlbums))
        })

        useCase.invoke().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(testAlbums, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure result on exception`() = runTest {
        val testException = Exception("Network error")

        whenever(repository.getAlbums()).thenReturn(flow {
            emit(Result.failure(testException))
        })

        useCase.invoke().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals("Network error", result.exceptionOrNull()?.message)
            awaitComplete()
        }
    }
}

class GetPhotosUseCaseTest {

    private lateinit var repository: HomeRepository
    private lateinit var useCase: GetPhotosUseCase

    @Before
    fun setUp() {
        repository = mock()
        useCase = GetPhotosUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns success result with photos`() = runTest {
        val testPhotos = listOf(
            Photo(id = 1, albumId = 1, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
            Photo(id = 2, albumId = 1, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2")
        )

        whenever(repository.getPhotos()).thenReturn(flow {
            emit(Result.success(testPhotos))
        })

        useCase.invoke().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(testPhotos, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure result on exception`() = runTest {
        val testException = Exception("Failed to fetch photos")

        whenever(repository.getPhotos()).thenReturn(flow {
            emit(Result.failure(testException))
        })

        useCase.invoke().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals("Failed to fetch photos", result.exceptionOrNull()?.message)
            awaitComplete()
        }
    }
}

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
}
