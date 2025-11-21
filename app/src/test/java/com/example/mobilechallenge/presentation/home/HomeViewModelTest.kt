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
    fun `loading initial page populates albums without photos`() = runTest {
        val testAlbums = listOf(
            Album(id = 1, userId = 1, title = "Album 1"),
            Album(id = 2, userId = 1, title = "Album 2"),
            Album(id = 3, userId = 1, title = "Album 3"),
            Album(id = 4, userId = 1, title = "Album 4"),
            Album(id = 5, userId = 1, title = "Album 5")
        )

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.success(testAlbums))
        })

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase)

        viewModel.uiState.test {
            skipItems(1)

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(5, state.albumsWithPhotos.size)
            assertTrue(state.albumsWithPhotos.all { it.photos.isEmpty() })
            assertNull(state.error)
        }
    }

    @Test
    fun `loadNextPage adds next page of albums`() = runTest {
        val testAlbums = listOf(
            Album(id = 1, userId = 1, title = "Album 1"),
            Album(id = 2, userId = 1, title = "Album 2"),
            Album(id = 3, userId = 1, title = "Album 3"),
            Album(id = 4, userId = 1, title = "Album 4"),
            Album(id = 5, userId = 1, title = "Album 5"),
            Album(id = 6, userId = 1, title = "Album 6"),
            Album(id = 7, userId = 1, title = "Album 7"),
            Album(id = 8, userId = 1, title = "Album 8"),
            Album(id = 9, userId = 1, title = "Album 9"),
            Album(id = 10, userId = 1, title = "Album 10")
        )

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.success(testAlbums))
        })

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading state
            awaitItem() // First page loaded

            viewModel.loadNextPage()

            val state = awaitItem()
            assertEquals(10, state.albumsWithPhotos.size)
            assertEquals("Album 1", state.albumsWithPhotos[0].album.title)
            assertEquals("Album 10", state.albumsWithPhotos[9].album.title)
        }
    }

    @Test
    fun `loadPhotosForAlbum lazy loads photos for specific album`() = runTest {
        val testAlbums = listOf(
            Album(id = 1, userId = 1, title = "Album 1")
        )
        val testPhotos = listOf(
            Photo(id = 1, albumId = 1, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
            Photo(id = 2, albumId = 1, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2")
        )

        whenever(getAlbumsUseCase.invoke()).thenReturn(flow {
            emit(Result.success(testAlbums))
        })
        whenever(getPhotosUseCase.invoke(1)).thenReturn(flow {
            emit(Result.success(testPhotos))
        })

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase)

        viewModel.uiState.test {
            skipItems(1) // Skip initial loading
            awaitItem() // Albums loaded without photos

            viewModel.loadPhotosForAlbum(1)

            val state = awaitItem()
            assertEquals(testPhotos, state.albumsWithPhotos[0].photos)
            assertFalse(state.loadingPhotoIds.contains(1))
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
