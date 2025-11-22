package com.example.mobilechallenge.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.usecase.GetAlbumsUseCase
import com.example.mobilechallenge.domain.usecase.GetPhotosUseCase
import com.example.mobilechallenge.util.NetworkConnectivityMonitor
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
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
    private lateinit var networkMonitor: NetworkConnectivityMonitor
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        getAlbumsUseCase = mock()
        getPhotosUseCase = mock()
        networkMonitor = mock()
        // Mock the network connectivity monitor to always return connected = true
        whenever(networkMonitor.observeConnectivityChanges()).thenReturn(
            flow { emit(true) }
        )
    }

    @Test
    fun `initial state is loading`() = runTest {
        // Setup mock to never emit
        whenever(getAlbumsUseCase.invoke(any(), any())).thenReturn(flow {})

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase, networkMonitor)

        assertTrue(viewModel.uiState.value.isLoading)
        assertEquals(emptyList(), viewModel.uiState.value.albumsWithPhotos)
        assertTrue(viewModel.uiState.value.hasMoreData)
    }

    @Test
    fun `loading first page succeeds and sets albums`() = runTest {
        val firstPageAlbums = listOf(
            Album(id = 1, userId = 1, title = "Album 1"),
            Album(id = 2, userId = 1, title = "Album 2"),
            Album(id = 3, userId = 1, title = "Album 3"),
            Album(id = 4, userId = 1, title = "Album 4"),
            Album(id = 5, userId = 1, title = "Album 5")
        )

        // Mock BEFORE creating ViewModel so init block gets mocked response
        getAlbumsUseCase = mock {
            onGeneric { invoke(any(), any()) }.thenAnswer {
                flow { emit(Result.success(firstPageAlbums)) }
            }
        }
        getPhotosUseCase = mock()

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase, networkMonitor)

        viewModel.uiState.test {
            // Skip initial "isLoading=true" state from init
            skipItems(1)
            
            // First emission: loading completes with albums
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(5, state.albumsWithPhotos.size)
            assertEquals("Album 1", state.albumsWithPhotos[0].album.title)
            assertNull(state.error)
            assertTrue(state.hasMoreData) // Full page, so more data available
        }
    }

    @Test
    fun `loading first page with full page size keeps hasMoreData true`() = runTest {
        val firstPageAlbums = (1..5).map { Album(id = it, userId = 1, title = "Album $it") }

        whenever(getAlbumsUseCase.invoke(any(), any())).thenReturn(
            flow { emit(Result.success(firstPageAlbums)) }
        )

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase, networkMonitor)

        viewModel.uiState.test {
            skipItems(1)

            val state = awaitItem()
            assertEquals(5, state.albumsWithPhotos.size)
            assertTrue(state.hasMoreData) // Got full page (5), so more data might exist
            assertNull(state.error)
        }
    }

    @Test
    fun `loading page with fewer items than page size sets hasMoreData to false`() = runTest {
        val firstPageAlbums = (1..5).map { Album(id = it, userId = 1, title = "Album $it") }
        val lastPageAlbums = (6..8).map { Album(id = it, userId = 1, title = "Album $it") }

        getAlbumsUseCase = mock {
            onGeneric { invoke(any(), any()) }.thenAnswer { invocation ->
                val start = invocation.getArgument<Int>(1)
                flow {
                    val result = if (start == 0) firstPageAlbums else lastPageAlbums
                    emit(Result.success(result))
                }
            }
        }
        getPhotosUseCase = mock()

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase, networkMonitor)

        viewModel.uiState.test {
            // Skip initial "isLoading=true" state from init
            skipItems(1)

            // First emission: initial page load completes
            val firstState = awaitItem()
            assertEquals(5, firstState.albumsWithPhotos.size)
            assertTrue(firstState.hasMoreData) // Full page, more data may exist

            // Trigger async request for second page
            viewModel.loadAlbumsNextPage()

            // Skip "isLoadingMore=true" emission (intermediate loading state)
            skipItems(1)

            // Next emission: second page load completes with fewer items
            val secondState = awaitItem()
            assertEquals(8, secondState.albumsWithPhotos.size) // 5 + 3
            assertFalse(secondState.hasMoreData) // Got 3 items < 5, so end detected
        }
    }

    @Test
    fun `loadAlbumsNextPage appends new albums correctly`() = runTest {
        val firstPageAlbums = (1..5).map { Album(id = it, userId = 1, title = "Album $it") }
        val secondPageAlbums = (6..10).map { Album(id = it, userId = 1, title = "Album $it") }

        getAlbumsUseCase = mock {
            onGeneric { invoke(any(), any()) }.thenAnswer { invocation ->
                val start = invocation.getArgument<Int>(1)
                flow {
                    val result = if (start == 0) firstPageAlbums else secondPageAlbums
                    emit(Result.success(result))
                }
            }
        }
        getPhotosUseCase = mock()

        viewModel = HomeViewModel(getAlbumsUseCase, getPhotosUseCase, networkMonitor)

        viewModel.uiState.test {
            // Skip initial "isLoading=true" state
            skipItems(1)

            // First emission: initial page load
            val firstState = awaitItem()
            assertEquals(5, firstState.albumsWithPhotos.size)
            assertEquals("Album 1", firstState.albumsWithPhotos.first().album.title)

            // Trigger async request for second page
            viewModel.loadAlbumsNextPage()

            // Skip "isLoadingMore=true" emission (intermediate loading state)
            skipItems(1)

            // Next emission: second page appended to list
            val secondState = awaitItem()
            assertEquals(10, secondState.albumsWithPhotos.size) // 5 + 5 appended
            assertEquals(
                "Album 1",
                secondState.albumsWithPhotos.first().album.title
            ) // First unchanged
            assertEquals(
                "Album 10",
                secondState.albumsWithPhotos.last().album.title
            ) // Last from new page
        }
    }
}
