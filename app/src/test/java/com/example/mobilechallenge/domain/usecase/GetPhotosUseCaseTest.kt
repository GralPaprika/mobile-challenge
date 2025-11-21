package com.example.mobilechallenge.domain.usecase

import app.cash.turbine.test
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPhotosUseCaseTest {

    private lateinit var repository: HomeRepository
    private lateinit var useCase: GetPhotosUseCase

    @Before
    fun setUp() {
        repository = mock()
        useCase = GetPhotosUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns success result with photos for album`() = runTest {
        val albumId = 1
        val testPhotos = listOf(
            Photo(id = 1, albumId = albumId, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
            Photo(id = 2, albumId = albumId, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2")
        )

        // Mock with flexible parameter matching
        repository = mock {
            onGeneric { getPhotosByAlbumId(any(), any(), any()) }.thenAnswer {
                flow<Result<List<Photo>>> { emit(Result.success(testPhotos)) }
            }
        }
        useCase = GetPhotosUseCaseImpl(repository)

        useCase.invoke(albumId).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(testPhotos, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `invoke with pagination parameters returns paginated results`() = runTest {
        val albumId = 1
        val limit = 10
        val start = 0
        val testPhotos = (1..10).map { 
            Photo(id = it, albumId = albumId, title = "Photo $it", url = "url$it", thumbnailUrl = "thumb$it")
        }

        repository = mock {
            onGeneric { getPhotosByAlbumId(any(), any(), any()) }.thenAnswer {
                flow { emit(Result.success(testPhotos)) }
            }
        }
        useCase = GetPhotosUseCaseImpl(repository)

        useCase.invoke(albumId, limit, start).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(10, result.getOrNull()?.size)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure result on exception`() = runTest {
        val albumId = 1
        val testException = Exception("Failed to fetch photos")

        repository = mock {
            onGeneric { getPhotosByAlbumId(any(), any(), any()) }.thenAnswer {
                flow<Result<List<Photo>>> { emit(Result.failure(testException)) }
            }
        }
        useCase = GetPhotosUseCaseImpl(repository)

        useCase.invoke(albumId).test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals("Failed to fetch photos", result.exceptionOrNull()?.message)
            awaitComplete()
        }
    }
}

