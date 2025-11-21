package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class SkeletonPhotoCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun skeletonPhotoCard_rendersSuccessfully() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonPhotoCard()
            }
        }

        composeTestRule.onRoot().printToLog("SkeletonPhotoCard")
        composeTestRule.waitForIdle()
    }

    @Test
    fun skeletonPhotoCard_withCustomModifier() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonPhotoCard(modifier = androidx.compose.ui.Modifier)
            }
        }

        composeTestRule.waitForIdle()
    }
}
