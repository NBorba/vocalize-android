package com.nborba.vocalize.ui.compose.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verifyHomeScreenContentIsDisplayed() {
        composeTestRule.setContent {
            HomeScreen(
                onNavigateToDetail = {},
            )
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
            HomeScreen(
                onNavigateToDetail = { id ->
                    callbackCalled = true
                    capturedId = id
                },
            )
        }

        composeTestRule.onNodeWithText("See details").performClick()

        assertTrue("Expected onNavigateToDetail callback to be invoked", callbackCalled)
        assertTrue("Expected captured ID to not be empty", !capturedId.isNullOrEmpty())
    }
}
