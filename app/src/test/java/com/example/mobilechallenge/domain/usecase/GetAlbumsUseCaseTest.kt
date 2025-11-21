package com.example.mobilechallenge.domain.usecase

import app.cash.turbine.test
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.repository.HomeRepository
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
