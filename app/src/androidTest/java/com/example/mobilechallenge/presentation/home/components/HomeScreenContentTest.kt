package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.mobilechallenge.data.model.Album
import com.example.mobilechallenge.data.model.Photo
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testAlbums = listOf(
        Album(id = 1, userId = 1, title = "Album 1"),
        Album(id = 2, userId = 1, title = "Album 2")
    )

    private val testPhotos = listOf(
        Photo(id = 1, albumId = 1, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
        Photo(id = 2, albumId = 1, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2")
    )

    @Test
    fun successScreen_displaysAlbumTitles() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                HomeScreenContent(
                    albums = testAlbums,
                    photos = testPhotos,
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Album 1").assertExists()
        composeTestRule.onNodeWithText("Album 2").assertExists()
    }

    @Test
    fun successScreen_displaysPhotosForAlbum() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                HomeScreenContent(
                    albums = testAlbums,
                    photos = testPhotos,
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Photo 1").assertExists()
        composeTestRule.onNodeWithText("Photo 2").assertExists()
    }

    @Test
    fun successScreen_withSingleAlbum() {
        val singleAlbum = listOf(testAlbums[0])
        val singlePhoto = listOf(testPhotos[0])

        composeTestRule.setContent {
            MobileChallengeTheme {
                HomeScreenContent(
                    albums = singleAlbum,
                    photos = singlePhoto,
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Album 1").assertExists()
        composeTestRule.onNodeWithText("Photo 1").assertExists()
    }

    @Test
    fun successScreen_withEmptyAlbums() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                HomeScreenContent(
                    albums = emptyList(),
                    photos = emptyList(),
                    onPhotoClick = {}
                )
            }
        }

        // Verify it renders without crashing with empty data
        composeTestRule.waitForIdle()
    }
}
