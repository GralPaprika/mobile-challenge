package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class PhotoCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testPhoto = Photo(
        id = 1,
        albumId = 1,
        title = "Mountain Peak",
        url = "https://via.placeholder.com/600",
        thumbnailUrl = "https://via.placeholder.com/150"
    )

    @Test
    fun photoCard_displaysPhotoTitle() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                PhotoCard(photo = testPhoto, onPhotoClick = {})
            }
        }

        composeTestRule.onNodeWithText("Mountain Peak").assertExists()
    }

    @Test
    fun photoCard_truncatesLongTitle() {
        val photoWithLongTitle = testPhoto.copy(title = "This is a very long title that should be truncated")

        composeTestRule.setContent {
            MobileChallengeTheme {
                PhotoCard(photo = photoWithLongTitle, onPhotoClick = {})
            }
        }

        composeTestRule.onNodeWithText("This is a very long ").assertExists()
    }

    @Test
    fun photoCard_callsCallbackOnClick() {
        var clickedPhoto: Photo? = null

        composeTestRule.setContent {
            MobileChallengeTheme {
                PhotoCard(photo = testPhoto, onPhotoClick = { photo ->
                    clickedPhoto = photo
                })
            }
        }

        composeTestRule.onNodeWithText("Mountain Peak").performClick()

        assert(clickedPhoto == testPhoto)
    }
}
