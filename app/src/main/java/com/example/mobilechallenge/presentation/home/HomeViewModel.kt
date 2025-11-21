package com.example.mobilechallenge.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val albums = getAlbumsUseCase.invoke().let { flow ->
                    var result = emptyList<com.example.mobilechallenge.domain.model.Album>()
                    flow.collect { resultWrapper ->
                        result = resultWrapper.getOrThrow()
                    }
                    result
                }

                val albumPhotosMap = mutableMapOf<Int, List<com.example.mobilechallenge.domain.model.Photo>>()
                for (album in albums) {
                    getPhotosUseCase.invoke(album.id).collect { result ->
                        val photos = result.getOrNull() ?: emptyList()
                        albumPhotosMap[album.id] = photos
                    }
                }

                val albumsWithPhotos = albums.map { album ->
                    AlbumWithPhotos(
                        album = album,
                        photos = albumPhotosMap[album.id] ?: emptyList()
                    )
                }

                _uiState.value = HomeUiState(
                    isLoading = false,
                    albumsWithPhotos = albumsWithPhotos,
                    error = null,
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    albumsWithPhotos = emptyList(),
                    error = e.message,
                )
            }
        }
    }
}
