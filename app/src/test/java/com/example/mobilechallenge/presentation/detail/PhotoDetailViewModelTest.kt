package com.example.mobilechallenge.presentation.detail

import app.cash.turbine.test
import com.example.mobilechallenge.domain.usecase.GetPhotoDescriptionUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class PhotoDetailViewModelTest {

    private lateinit var getPhotoDescriptionUseCase: GetPhotoDescriptionUseCase
    private lateinit var viewModel: PhotoDetailViewModel

    @Before
    fun setUp() {
        getPhotoDescriptionUseCase = mock()
        viewModel = PhotoDetailViewModel(getPhotoDescriptionUseCase)
    }

    @Test
    fun `getPhotoDescription returns description from use case`() = runTest {
        val expectedDescription = "This is a test description"
        whenever(getPhotoDescriptionUseCase.invoke()).thenReturn(
            flow { emit(Result.success(expectedDescription)) }
        )

        viewModel.getPhotoDescription().test {
            val result = awaitItem()
            assertEquals(expectedDescription, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `getPhotoDescription handles error from use case`() = runTest {
        val expectedError = Exception("Test error")
        whenever(getPhotoDescriptionUseCase.invoke()).thenReturn(
            flow { emit(Result.failure(expectedError)) }
        )

        viewModel.getPhotoDescription().test {
            val result = awaitItem()
            assertEquals(expectedError, result.exceptionOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `getPhotoDescription returns empty string on null`() = runTest {
        whenever(getPhotoDescriptionUseCase.invoke()).thenReturn(
            flow { emit(Result.success("")) }
        )

        viewModel.getPhotoDescription().test {
            val result = awaitItem()
            assertEquals("", result.getOrNull())
            awaitComplete()
        }
    }
}
