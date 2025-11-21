package com.example.mobilechallenge.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.domain.usecase.GetAlbumsUseCase
import com.example.mobilechallenge.domain.usecase.GetPhotosUseCase
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

class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getAlbumsUseCase: GetAlbumsUseCase
    private lateinit var getPhotosUseCase: GetPhotosUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        getAlbumsUseCase = mock()
        getPhotosUseCase = mock()
    }

    @Test
    fun `initial state is loading`() = runTest {
        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {})

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase)

        assertTrue(viewModel.uiState.value.isLoading)
        assertEquals(emptyList(), viewModel.uiState.value.albumsWithPhotos)
    }

    @Test
    fun `loading data successfully fetches photos and updates state`() = runTest {
        val testAlbums = listOf(
            Album(id = 1, userId = 1, title = "Album 1"),
            Album(id = 2, userId = 1, title = "Album 2")
        )
        val testPhotos1 = listOf(
            Photo(id = 1, albumId = 1, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
            Photo(id = 2, albumId = 1, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2")
        )
        val testPhotos2 = listOf(
            Photo(id = 3, albumId = 2, title = "Photo 3", url = "url3", thumbnailUrl = "thumb3")
        )

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.success(testAlbums))
        })
        whenever(getPhotosUseCase.invoke(1)).thenReturn(flow {
            emit(Result.success(testPhotos1))
        })
        whenever(getPhotosUseCase.invoke(2)).thenReturn(flow {
            emit(Result.success(testPhotos2))
        })

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase)

        viewModel.uiState.test {
            skipItems(1)

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.albumsWithPhotos.size)
            assertEquals(testPhotos1, state.albumsWithPhotos[0].photos)
            assertEquals(testPhotos2, state.albumsWithPhotos[1].photos)
            assertNull(state.error)
        }
    }

    @Test
    fun `loading data fails updates state with error`() = runTest {
        val testException = Exception("Network error")

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.failure(testException))
        })

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase)

        viewModel.uiState.test {
            skipItems(1)

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("Network error", state.error)
            assertEquals(emptyList(), state.albumsWithPhotos)
        }
    }
}
