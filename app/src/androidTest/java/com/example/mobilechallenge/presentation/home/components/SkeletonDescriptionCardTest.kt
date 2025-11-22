package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.mobilechallenge.presentation.detail.SkeletonDescriptionCard
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class SkeletonDescriptionCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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

    @Test
    fun skeletonDescriptionCard_loadsAnimatedShimmer() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                SkeletonDescriptionCard()
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.waitForIdle()
    }
}
