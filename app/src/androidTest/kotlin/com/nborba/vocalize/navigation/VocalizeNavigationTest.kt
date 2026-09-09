package com.nborba.vocalize.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.nborba.vocalize.MainActivity
import org.junit.Rule
import org.junit.Test

// TODO: Once business logic is implemented with ViewModels,
//  we will need to provide mocked hilt modules to have consistent testing conditions.
class VocalizeNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fullNavigationFlow_homeToDetailAndBack() {
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
