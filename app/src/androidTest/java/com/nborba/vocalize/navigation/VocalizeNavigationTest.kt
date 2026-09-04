package com.nborba.vocalize.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import com.nborba.vocalize.ui.VocalizeApp
import org.junit.Rule
import org.junit.Test

class VocalizeNavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun fullNavigationFlow_homeToDetailAndBack() {
        composeTestRule.setContent {
            VocalizeTheme {
                VocalizeApp()
            }
        }

        // 1. Verify Home Screen is displayed
        composeTestRule.onNodeWithText("Welcome to the app!").assertIsDisplayed()

        // 2. Click "See details" to navigate to DetailScreen
        composeTestRule.onNodeWithText("See details").performClick()

        // 3. Verify Detail Screen is displayed
        composeTestRule.onNodeWithText("Viewing detail").assertIsDisplayed()

        // 4. Click TopBar Back Arrow to navigate back to HomeScreen
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // 5. Verify Home Screen is displayed again
        composeTestRule.onNodeWithText("Welcome to the app!").assertIsDisplayed()
    }

    @Test
    fun fullNavigationFlow_homeToRecorderAndBack() {
        composeTestRule.setContent {
            VocalizeTheme {
                VocalizeApp()
            }
        }

        // 1. Verify Home Screen is displayed
        composeTestRule.onNodeWithText("Welcome to the app!").assertIsDisplayed()

        // 2. Click "Record" to navigate to Recorder
        composeTestRule.onNodeWithText("Record").performClick()

        // 3. Verify Recorder is displayed
        composeTestRule.onNodeWithText("Recorder").assertIsDisplayed()

        // 4. Press back to dismiss
        Espresso.pressBack()

        // 5. Verify Home Screen is displayed again
        composeTestRule.onNodeWithText("Welcome to the app!").assertIsDisplayed()
    }
}
