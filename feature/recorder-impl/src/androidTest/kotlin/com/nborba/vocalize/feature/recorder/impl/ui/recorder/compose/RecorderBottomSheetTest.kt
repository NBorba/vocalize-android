package com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso
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
                RecorderBottomSheet(
                    onDismissRequest = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Recorder").assertIsDisplayed()
    }

    @Test
    fun triggeringBackAction_triggersDismissRequest() {
        var dismissTriggered = false

        composeTestRule.setContent {
            VocalizeTheme {
                RecorderBottomSheet(
                    onDismissRequest = {
                        dismissTriggered = true
                    },
                )
            }
        }

        Espresso.pressBack()

        assertTrue("Expected onDismissRequest callback to be invoked", dismissTriggered)
    }
}
