package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.hasTestTag
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class NoConnectionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noConnectionScreen_displaysWifiOffIcon() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                NoConnectionScreen()
            }
        }

        composeTestRule.onNode(hasTestTag("wifi_off_icon")).assertExists()
    }

    @Test
    fun noConnectionScreen_displaysErrorMessage() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                NoConnectionScreen()
            }
        }

        composeTestRule.onNodeWithText("No Internet Connection").assertExists()
    }

    @Test
    fun noConnectionScreen_displaysErrorDescription() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                NoConnectionScreen()
            }
        }

        composeTestRule.onNodeWithText("Please check your internet connection and try again").assertExists()
    }
}
