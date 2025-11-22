package com.example.mobilechallenge.presentation.home.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import org.junit.Rule
import org.junit.Test

class LoadingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingScreen_displaysCircularProgressIndicator() {
        composeTestRule.setContent {
            MobileChallengeTheme {
                LoadingScreen()
            }
        }

        composeTestRule.onRoot().printToLog("LoadingScreen")
    }
}
