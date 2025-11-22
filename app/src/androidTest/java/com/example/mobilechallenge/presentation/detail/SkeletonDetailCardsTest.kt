package com.example.mobilechallenge.presentation.detail

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class SkeletonDetailCardsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun skeletonPhotoDetailImage_rendersSuccessfully() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonPhotoDetailImage()
            }
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun skeletonPhotoDetailImage_withCustomModifier() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonPhotoDetailImage(modifier = androidx.compose.ui.Modifier)
            }
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun skeletonDescriptionCard_rendersSuccessfully() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonDescriptionCard()
            }
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun skeletonDescriptionCard_withCustomModifier() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonDescriptionCard(modifier = androidx.compose.ui.Modifier)
            }
        }

        composeTestRule.waitForIdle()
    }
}
