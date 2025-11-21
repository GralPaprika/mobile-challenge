package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.hasTestTag
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class SkeletonAlbumCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun skeletonAlbumCard_rendersSuccessfully() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonAlbumCard()
            }
        }

        composeTestRule.onNode(hasTestTag("skeleton_album_card")).assertExists()
    }

    @Test
    fun skeletonAlbumCard_withCustomModifier() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonAlbumCard(modifier = androidx.compose.ui.Modifier.fillMaxWidth())
            }
        }

        composeTestRule.onNode(hasTestTag("skeleton_album_card")).assertExists()
    }
}
