package com.example.mobilechallenge.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.domain.usecase.GetAlbumsUseCase
import com.example.mobilechallenge.domain.usecase.GetPhotosUseCase
import com.example.mobilechallenge.presentation.home.model.AlbumWithPhotos
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
    val hasMoreData: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val photoCache = mutableMapOf<Int, List<Photo>>()
    private var currentPage = 0
    private val pageSize = 5

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                currentPage = 0
                loadNextPage()
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    albumsWithPhotos = emptyList(),
                    error = e.message,
                )
            }
        }
    }

    fun loadNextPage() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMoreData) return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoadingMore = true)

                val start = currentPage * pageSize
                getAlbumsUseCase.invoke(pageSize, start).collect { result ->
                    val albums = result.getOrThrow()
                    
                    // Check if we've reached the end (got fewer items than page size)
                    val isLastPage = albums.size < pageSize
                    
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
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    error = e.message
                )
            }
        }
    }

    fun loadPhotosForAlbum(albumId: Int) {
        if (photoCache.containsKey(albumId)) return

        viewModelScope.launch {
            val currentLoadingIds = _uiState.value.loadingPhotoIds
            _uiState.value = _uiState.value.copy(loadingPhotoIds = currentLoadingIds + albumId)

            try {
                getPhotosUseCase.invoke(albumId).collect { result ->
                    val photos = result.getOrNull() ?: emptyList()
                    photoCache[albumId] = photos

                    val updatedAlbums = _uiState.value.albumsWithPhotos.map { albumWithPhotos ->
                        if (albumWithPhotos.album.id == albumId) {
                            albumWithPhotos.copy(photos = photos)
                        } else {
                            albumWithPhotos
                        }
                    }

                    val updatedLoadingIds = _uiState.value.loadingPhotoIds - albumId
                    _uiState.value = _uiState.value.copy(
                        albumsWithPhotos = updatedAlbums,
                        loadingPhotoIds = updatedLoadingIds
                    )
                }
            } catch (e: Exception) {
                val updatedLoadingIds = _uiState.value.loadingPhotoIds - albumId
                _uiState.value = _uiState.value.copy(loadingPhotoIds = updatedLoadingIds)
            }
        }
    }
}
