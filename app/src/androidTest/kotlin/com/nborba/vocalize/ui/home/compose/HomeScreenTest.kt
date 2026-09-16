package com.nborba.vocalize.ui.home.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.nborba.vocalize.HomeUiStateFixture
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
                HomeContent(
                    state = HomeUiStateFixture.default(),
                    onRecordButtonClick = {},
                )
            }
        }

        // TopAppBar Title
        composeTestRule.onNodeWithText("Vocalize").assertIsDisplayed()

        // Empty State Title & Description
        composeTestRule.onNodeWithText("No recordings yet").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Tap Record below to start your first recording.", substring = true)
            .assertIsDisplayed()

        // Floating Action Record Button
        composeTestRule.onNodeWithTag(HOME_RECORD_FAB_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun clickingRecordButton_triggersCallback() {
        var callbackCalled = false

        composeTestRule.setContent {
            VocalizeTheme {
                HomeContent(
                    state = HomeUiStateFixture.default(),
                    onRecordButtonClick = {
                        callbackCalled = true
                    },
                )
            }
        }

        composeTestRule.onNodeWithTag(HOME_RECORD_FAB_TEST_TAG).performClick()

        assertTrue("Expected onRecordButtonClick callback to be invoked", callbackCalled)
    }
}
