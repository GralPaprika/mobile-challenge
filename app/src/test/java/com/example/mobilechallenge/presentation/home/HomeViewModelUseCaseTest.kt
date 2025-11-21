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

class HomeViewModelUseCaseTest {

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
    fun `view model orchestrates album and photo loading`() = runTest {
        val album1 = Album(id = 1, userId = 1, title = "Album 1")
        val album2 = Album(id = 2, userId = 1, title = "Album 2")
        val photos1 = listOf(
            Photo(id = 1, albumId = 1, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
            Photo(id = 2, albumId = 1, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2")
        )
        val photos2 = listOf(
            Photo(id = 3, albumId = 2, title = "Photo 3", url = "url3", thumbnailUrl = "thumb3")
        )

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.success(listOf(album1, album2)))
        })
        whenever(getPhotosUseCase.invoke(1)).thenReturn(flow {
            emit(Result.success(photos1))
        })
        whenever(getPhotosUseCase.invoke(2)).thenReturn(flow {
            emit(Result.success(photos2))
        })

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase)

        viewModel.uiState.test {
            skipItems(1)

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.albumsWithPhotos.size)
            assertEquals("Album 1", state.albumsWithPhotos[0].album.title)
            assertEquals(photos1, state.albumsWithPhotos[0].photos)
            assertEquals("Album 2", state.albumsWithPhotos[1].album.title)
            assertEquals(photos2, state.albumsWithPhotos[1].photos)
            assertNull(state.error)
        }
    }

    @Test
    fun `loading fails when use case returns error`() = runTest {
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
