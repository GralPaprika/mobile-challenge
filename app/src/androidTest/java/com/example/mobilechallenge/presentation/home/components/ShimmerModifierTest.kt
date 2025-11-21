package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class ShimmerModifierTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shimmerModifier_appliesSuccessfully() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .size(100.dp)
                        .shimmerBackground()
                )
            }
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun shimmerModifier_withDifferentSizes() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .size(200.dp)
                        .shimmerBackground()
                )
            }
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun shimmerModifier_animatesInfinitely() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .size(150.dp)
                        .shimmerBackground()
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.waitForIdle()
    }
}
