package com.example.mobilechallenge.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val albums: List<Album> = emptyList(),
    val photos: List<Photo> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.getAlbums(),
                repository.getPhotos()
            ) { albumsResult, photosResult ->
                Pair(albumsResult, photosResult)
            }.collect { (albumsResult, photosResult) ->
                val albumsData = albumsResult.getOrNull() ?: emptyList()
                val photosData = photosResult.getOrNull() ?: emptyList()
                val error = albumsResult.exceptionOrNull()?.message
                    ?: photosResult.exceptionOrNull()?.message

                _uiState.value = HomeUiState(
                    isLoading = false,
                    albums = albumsData,
                    photos = photosData,
                    error = error,
                )
            }
        }
    }
}
