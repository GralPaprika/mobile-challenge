package com.example.mobilechallenge.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallenge.domain.constant.PaginationConstants.ALBUM_PAGE_SIZE
import com.example.mobilechallenge.domain.constant.PaginationConstants.PAGINATION_START_INDEX
import com.example.mobilechallenge.domain.constant.PaginationConstants.PHOTO_PAGE_SIZE
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.domain.usecase.GetAlbumsUseCase
import com.example.mobilechallenge.domain.usecase.GetPhotosUseCase
import com.example.mobilechallenge.presentation.home.model.AlbumWithPhotos
import com.example.mobilechallenge.util.NetworkConnectivityMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val albumsWithPhotos: List<AlbumWithPhotos> = emptyList(),
    val isLoadingMore: Boolean = false,
    val loadingPhotoIds: Set<Int> = emptySet(),
    val error: String? = null,
    val hasMoreData: Boolean = true,
    val isNetworkError: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val getPhotosUseCase: GetPhotosUseCase,
    private val networkConnectivityMonitor: NetworkConnectivityMonitor
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val photoCache = mutableMapOf<Int, List<Photo>>()
    private val photoPaginationState = mutableMapOf<Int, PhotoPaginationState>()
    private var currentPage = PAGINATION_START_INDEX

    data class PhotoPaginationState(
        var currentPage: Int = 0,
        var hasMorePhotos: Boolean = true,
        var isLoadingMorePhotos: Boolean = false
    )

    init {
        loadInitialData()
        observeConnectivityChanges()
    }

    private fun observeConnectivityChanges() {
        viewModelScope.launch {
            networkConnectivityMonitor.observeConnectivityChanges().collect { isConnected ->
                if (!isConnected) {
                    _uiState.value = _uiState.value.copy(isNetworkError = true)
                } else {
                    // Only clear the network error if there's no other error
                    if (_uiState.value.error == null) {
                        _uiState.value = _uiState.value.copy(isNetworkError = false)
                    }
                }
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                currentPage = 0
                loadAlbumsNextPage()
            } catch (e: Exception) {
                val isNetworkError = isNetworkException(e)
                _uiState.value = HomeUiState(
                    isLoading = false,
                    albumsWithPhotos = emptyList(),
                    error = e.message,
                    isNetworkError = isNetworkError
                )
            }
        }
    }

    fun loadAlbumsNextPage() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMoreData) return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoadingMore = true)

                val start = currentPage * ALBUM_PAGE_SIZE
                getAlbumsUseCase.invoke(ALBUM_PAGE_SIZE, start).collect { result ->
                    val albums = result.getOrThrow()
                    
                    // Check if we've reached the end (got fewer items than page size)
                    val isLastPage = albums.size < ALBUM_PAGE_SIZE
                    
                    if (albums.isNotEmpty()) {
                        val newAlbumsWithPhotos = albums.map { album ->
                            val photos = photoCache.getOrElse(album.id) { emptyList() }
                            AlbumWithPhotos(album = album, photos = photos)
                        }

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            albumsWithPhotos = _uiState.value.albumsWithPhotos + newAlbumsWithPhotos,
                            isLoadingMore = false,
                            hasMoreData = !isLastPage
                        )
                        if (!isLastPage) {
                            currentPage++
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoadingMore = false,
                            hasMoreData = false
                        )
                    }
                }
            } catch (e: Exception) {
                val isNetworkError = isNetworkException(e)
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    error = e.message,
                    isNetworkError = isNetworkError
                )
            }
        }
    }

    fun loadPhotosForAlbum(albumId: Int) {
        val paginationState = photoPaginationState.getOrPut(albumId) { PhotoPaginationState() }
        
        // Prevent loading if already loading or if we've reached the end
        if (paginationState.isLoadingMorePhotos || !paginationState.hasMorePhotos) return

        viewModelScope.launch {
            paginationState.isLoadingMorePhotos = true
            updateLoadingPhotoIds(albumId, isLoading = true)

            try {
                val start = paginationState.currentPage * PHOTO_PAGE_SIZE
                getPhotosUseCase.invoke(albumId, PHOTO_PAGE_SIZE, start).collect { result ->
                    val newPhotos = result.getOrNull() ?: emptyList()
                    val isLastPage = newPhotos.size < PHOTO_PAGE_SIZE
                    
                    if (newPhotos.isNotEmpty()) {
                        updatePhotosForAlbum(albumId, newPhotos)
                        if (!isLastPage) {
                            paginationState.currentPage++
                        }
                    }
                    
                    paginationState.hasMorePhotos = !isLastPage
                    paginationState.isLoadingMorePhotos = false
                    updateLoadingPhotoIds(albumId, isLoading = false)
                }
            } catch (e: Exception) {
                paginationState.isLoadingMorePhotos = false
                updateLoadingPhotoIds(albumId, isLoading = false)
            }
        }
    }

    private fun updatePhotosForAlbum(albumId: Int, newPhotos: List<Photo>) {
        val currentPhotos = photoCache.getOrDefault(albumId, emptyList())
        val allPhotos = currentPhotos + newPhotos
        photoCache[albumId] = allPhotos

        val currentAlbums = _uiState.value.albumsWithPhotos.toMutableList()
        val albumIndex = currentAlbums.indexOfFirst { it.album.id == albumId }
        
        if (albumIndex >= 0) {
            currentAlbums[albumIndex] = currentAlbums[albumIndex].copy(photos = allPhotos)
            _uiState.value = _uiState.value.copy(albumsWithPhotos = currentAlbums)
        }
    }

    private fun updateLoadingPhotoIds(albumId: Int, isLoading: Boolean) {
        val updatedLoadingIds = if (isLoading) {
            _uiState.value.loadingPhotoIds + albumId
        } else {
            _uiState.value.loadingPhotoIds - albumId
        }
        _uiState.value = _uiState.value.copy(loadingPhotoIds = updatedLoadingIds)
    }

    private fun isNetworkException(exception: Exception): Boolean {
        return exception is java.net.SocketException ||
                exception is java.net.SocketTimeoutException ||
                exception is java.io.IOException ||
                exception.cause is java.net.SocketException ||
                exception.cause is java.net.SocketTimeoutException ||
                exception.cause is java.io.IOException ||
                exception.message?.contains("Unable to resolve host", ignoreCase = true) == true ||
                exception.message?.contains("Connection refused", ignoreCase = true) == true ||
                exception.message?.contains("No address associated", ignoreCase = true) == true ||
                exception.message?.contains("Network is unreachable", ignoreCase = true) == true ||
                exception.message?.contains("Connection reset", ignoreCase = true) == true ||
                exception.message?.contains("timeout", ignoreCase = true) == true
    }
}
