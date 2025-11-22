package com.example.mobilechallenge.presentation.detail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import com.example.mobilechallenge.domain.usecase.GetPhotoDescriptionUseCase
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PhotoDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: PhotoDetailViewModel

    @Before
    fun setUp() {
        val mockUseCase = mock<GetPhotoDescriptionUseCase>()
        mockViewModel = mock<PhotoDetailViewModel>()
    }

    @Test
    fun `photoDetailScreen_displaysPhotoTitle`() {
        whenever(mockViewModel.getPhotoDescription()).thenReturn(
            flowOf(Result.success("Test Description"))
        )

        composeTestRule.setContent {
            PhotoDetailScreen(
                photoId = 1,
                photoUrl = "https://example.com/photo.jpg",
                photoThumbnailUrl = "https://example.com/thumb.jpg",
                photoTitle = "Test Photo Title",
                onBackClick = {},
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("Test Photo Title").assertIsDisplayed()
    }

    @Test
    fun `photoDetailScreen_displaysDescriptionHeader`() {
        whenever(mockViewModel.getPhotoDescription()).thenReturn(
            flowOf(Result.success("Test Description"))
        )

        composeTestRule.setContent {
            PhotoDetailScreen(
                photoId = 1,
                photoUrl = "https://example.com/photo.jpg",
                photoThumbnailUrl = "https://example.com/thumb.jpg",
                photoTitle = "Test Photo",
                onBackClick = {},
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun `photoDetailScreen_loadsDescription`() {
        val testDescription = "This is a detailed description"
        whenever(mockViewModel.getPhotoDescription()).thenReturn(
            flowOf(Result.success(testDescription))
        )

        composeTestRule.setContent {
            PhotoDetailScreen(
                photoId = 1,
                photoUrl = "https://example.com/photo.jpg",
                photoThumbnailUrl = "https://example.com/thumb.jpg",
                photoTitle = "Test Photo",
                onBackClick = {},
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText(testDescription).assertIsDisplayed()
    }

    @Test
    fun `photoDetailScreen_callsOnBackClickWhenBackPressed`() {
        var backClicked = false
        whenever(mockViewModel.getPhotoDescription()).thenReturn(
            flowOf(Result.success(""))
        )

        composeTestRule.setContent {
            PhotoDetailScreen(
                photoId = 1,
                photoUrl = "https://example.com/photo.jpg",
                photoThumbnailUrl = "https://example.com/thumb.jpg",
                photoTitle = "Test Photo",
                onBackClick = { backClicked = true },
                viewModel = mockViewModel
            )
        }

        // Find and click the back button
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        
        assert(backClicked)
    }
}
