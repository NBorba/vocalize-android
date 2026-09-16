package com.nborba.vocalize.navigation

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.rule.GrantPermissionRule
import com.nborba.vocalize.MainActivity
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose.RECORDER_MAIN_BUTTON_TEST_TAG
import com.nborba.vocalize.ui.home.compose.HOME_RECORD_FAB_TEST_TAG
import org.junit.Rule
import org.junit.Test

// TODO: Once business logic is implemented with ViewModels,
//  we will need to provide mocked hilt modules to have consistent testing conditions.
class VocalizeNavigationTest {
    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fullNavigationFlow_homeToRecorderAndBack() {
        // 1. Verify Home Screen is displayed
        composeTestRule.onNodeWithText("Vocalize").assertIsDisplayed()
        composeTestRule.onNodeWithText("No recordings yet").assertIsDisplayed()

        // 2. Click "Record" FAB to navigate to Recorder
        composeTestRule.onNodeWithTag(HOME_RECORD_FAB_TEST_TAG).performClick()

        // 3. Verify Recorder bottom sheet is displayed by targeting its testTag
        composeTestRule.onNodeWithTag(RECORDER_MAIN_BUTTON_TEST_TAG).assertIsDisplayed()

        // 4. Press back to dismiss
        Espresso.pressBack()

        // 5. Verify Home Screen is displayed again
        composeTestRule.onNodeWithText("Vocalize").assertIsDisplayed()
        composeTestRule.onNodeWithText("No recordings yet").assertIsDisplayed()
    }
}
