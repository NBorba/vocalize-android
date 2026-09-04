package com.nborba.vocalize.ui.home.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verifyHomeScreenContentIsDisplayed() {
        composeTestRule.setContent {
            VocalizeTheme {
                HomeScreen(
                    onNavigateToDetail = {},
                    onNavigateToRecorder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Vocalize").assertIsDisplayed()
        composeTestRule.onNodeWithText("Welcome to the app!").assertIsDisplayed()
        composeTestRule.onNodeWithText("See details").assertIsDisplayed()
    }

    @Test
    fun clickingSeeDetailsButton_triggersCallback() {
        var callbackCalled = false
        var capturedId: String? = null

        composeTestRule.setContent {
            VocalizeTheme {
                HomeScreen(
                    onNavigateToDetail = { id ->
                        callbackCalled = true
                        capturedId = id
                    },
                    onNavigateToRecorder = {},
                )
            }
        }

        composeTestRule.onNodeWithText("See details").performClick()

        assertTrue("Expected onNavigateToDetail callback to be invoked", callbackCalled)
        assertTrue("Expected captured ID to not be empty", !capturedId.isNullOrEmpty())
    }

    @Test
    fun clickingRecordButton_triggersCallback() {
        var callbackCalled = false

        composeTestRule.setContent {
            VocalizeTheme {
                HomeScreen(
                    onNavigateToDetail = {},
                    onNavigateToRecorder = {
                        callbackCalled = true
                    },
                )
            }
        }

        composeTestRule.onNodeWithText("Record").performClick()

        assertTrue("Expected onNavigateToRecorder callback to be invoked", callbackCalled)
    }
}
