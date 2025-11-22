package com.example.mobilechallenge.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.mobilechallenge.domain.usecase.GetPhotoDescriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
open class PhotoDetailViewModel @Inject constructor(
    private val getPhotoDescriptionUseCase: GetPhotoDescriptionUseCase
) : ViewModel() {

    open fun getPhotoDescription(): Flow<Result<String>> {
        return getPhotoDescriptionUseCase.invoke()
    }
}
