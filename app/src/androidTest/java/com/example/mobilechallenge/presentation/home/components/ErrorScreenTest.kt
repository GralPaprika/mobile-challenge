package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class ErrorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorScreen_displaysErrorTitle() {
        val errorMessage = "Network connection failed"

        composeTestRule.setContent {
            MobileChallengeTheme {
                ErrorScreen(error = errorMessage)
            }
        }

        composeTestRule.onNodeWithText("Error loading data").assertExists()
    }

    @Test
    fun errorScreen_displaysErrorMessage() {
        val errorMessage = "Failed to fetch data from API"

        composeTestRule.setContent {
            MobileChallengeTheme {
                ErrorScreen(error = errorMessage)
            }
        }

        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }
}
