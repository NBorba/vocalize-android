package com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.nborba.vocalize.RecorderUiStateFixture
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RecorderBottomSheetTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verifyRecorderScreenContentIsDisplayed() {
        composeTestRule.setContent {
            VocalizeTheme {
                RecorderContent(
                    uiState = RecorderUiStateFixture.default(),
                    onMainButtonClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag(RECORDER_MAIN_BUTTON_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun clickingMainButton_triggersCallback() {
        var callbackCalled = false

        composeTestRule.setContent {
            VocalizeTheme {
                RecorderContent(
                    uiState = RecorderUiStateFixture.default(),
                    onMainButtonClick = { callbackCalled = true },
                )
            }
        }

        composeTestRule.onNodeWithTag(RECORDER_MAIN_BUTTON_TEST_TAG).performClick()

        assertTrue("Expected onMainButtonClick callback to be invoked", callbackCalled)
    }
}
