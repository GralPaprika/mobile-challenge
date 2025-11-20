package com.example.mobilechallenge.domain.usecase

import app.cash.turbine.test
import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
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
