package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class CarouselSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testPhotos = listOf(
        Photo(id = 1, albumId = 1, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
        Photo(id = 2, albumId = 1, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2"),
        Photo(id = 3, albumId = 1, title = "Photo 3", url = "url3", thumbnailUrl = "thumb3")
    )

    @Test
    fun carouselSection_displaysTitle() {
        val sectionTitle = "Summer Vacation"

        composeTestRule.setContent {
            MobileChallengeTheme {
                CarouselSection(
                    title = sectionTitle,
                    photos = testPhotos,
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(sectionTitle).assertExists()
    }

    @Test
    fun carouselSection_displaysSinglePhoto() {
        val singlePhoto = listOf(testPhotos[0])

        composeTestRule.setContent {
            MobileChallengeTheme {
                CarouselSection(
                    title = "Album",
                    photos = singlePhoto,
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Photo 1").assertExists()
    }

    @Test
    fun carouselSection_displaysMultiplePhotos() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                CarouselSection(
                    title = "Album",
                    photos = testPhotos,
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Photo 1").assertExists()
        composeTestRule.onNodeWithText("Photo 2").assertExists()
    }

    @Test
    fun carouselSection_withEmptyPhotos() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                CarouselSection(
                    title = "Empty Album",
                    photos = emptyList(),
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Empty Album").assertExists()
    }
}
