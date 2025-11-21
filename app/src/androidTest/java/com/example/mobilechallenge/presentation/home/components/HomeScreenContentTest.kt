package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.mobilechallenge.domain.model.Album
import com.example.mobilechallenge.domain.model.Photo
import com.example.mobilechallenge.presentation.home.model.AlbumWithPhotos
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testAlbumsWithPhotos = listOf(
        AlbumWithPhotos(
            album = Album(id = 1, userId = 1, title = "Album 1"),
            photos = listOf(
                Photo(id = 1, albumId = 1, title = "Photo 1", url = "url1", thumbnailUrl = "thumb1"),
                Photo(id = 2, albumId = 1, title = "Photo 2", url = "url2", thumbnailUrl = "thumb2")
            )
        ),
        AlbumWithPhotos(
            album = Album(id = 2, userId = 1, title = "Album 2"),
            photos = listOf(
                Photo(id = 3, albumId = 2, title = "Photo 3", url = "url3", thumbnailUrl = "thumb3")
            )
        )
    )

    @Test
    fun successScreen_displaysAlbumTitles() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                HomeScreenContent(
                    albumsWithPhotos = testAlbumsWithPhotos,
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
                    albumsWithPhotos = testAlbumsWithPhotos,
                    onPhotoClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Photo 1").assertExists()
        composeTestRule.onNodeWithText("Photo 3").assertExists()
    }

    @Test
    fun successScreen_withSingleAlbum() {
        val singleAlbum = listOf(testAlbumsWithPhotos[0])

        composeTestRule.setContent {
            MobileChallengeTheme {
                HomeScreenContent(
                    albumsWithPhotos = singleAlbum,
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
                    albumsWithPhotos = emptyList(),
                    onPhotoClick = {}
                )
            }
        }

        // Verify it renders without crashing with empty data
        composeTestRule.waitForIdle()
    }
}
