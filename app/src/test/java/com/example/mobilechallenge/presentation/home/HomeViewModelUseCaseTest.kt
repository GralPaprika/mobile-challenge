package com.example.mobilechallenge.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.domain.usecase.LoadHomeScreenUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HomeViewModelUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loadHomeScreenUseCase: LoadHomeScreenUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        loadHomeScreenUseCase = mock()
    }

    @Test
    fun `initial state is loading`() = runTest {
        whenever(loadHomeScreenUseCase.invoke()).thenReturn(flow {})

        viewModel = HomeViewModel(loadHomeScreenUseCase)

        assertTrue(viewModel.uiState.value.isLoading)
        assertEquals(emptyList(), viewModel.uiState.value.albums)
        assertEquals(emptyList(), viewModel.uiState.value.photos)
    }

    @Test
    fun `loading data successfully updates state with albums and photos`() = runTest {
        val testAlbums = listOf(
            Album(id = 1, userId = 1, title = "Album 1"),
            Album(id = 2, userId = 1, title = "Album 2")
        )
        val testPhotos = listOf(
            Photo(id = 1, albumId = 1, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
            Photo(id = 2, albumId = 1, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2")
        )

        whenever(loadHomeScreenUseCase.invoke()).thenReturn(flow {
            emit(Pair(Result.success(testAlbums), Result.success(testPhotos)))
        })

        viewModel = HomeViewModel(loadHomeScreenUseCase)

        viewModel.uiState.test {
            skipItems(1)

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(testAlbums, state.albums)
            assertEquals(testPhotos, state.photos)
            assertNull(state.error)
        }
    }

    @Test
    fun `loading albums fails updates state with error`() = runTest {
        val testException = Exception("Network error")

        whenever(loadHomeScreenUseCase.invoke()).thenReturn(flow {
            emit(Pair(Result.failure(testException), Result.success(emptyList())))
        })

        viewModel = HomeViewModel(loadHomeScreenUseCase)

        viewModel.uiState.test {
            skipItems(1)

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("Network error", state.error)
            assertEquals(emptyList(), state.albums)
        }
    }

    @Test
    fun `loading photos fails updates state with error`() = runTest {
        val testException = Exception("Failed to load photos")

        whenever(loadHomeScreenUseCase.invoke()).thenReturn(flow {
            emit(Pair(Result.success(emptyList()), Result.failure(testException)))
        })

        viewModel = HomeViewModel(loadHomeScreenUseCase)

        viewModel.uiState.test {
            skipItems(1)

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("Failed to load photos", state.error)
        }
    }
}
