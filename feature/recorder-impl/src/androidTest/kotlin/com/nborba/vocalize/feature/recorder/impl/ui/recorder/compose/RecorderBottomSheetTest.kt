package com.nborba.vocalize.feature.recorder.impl.ui.recorder.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import org.junit.Rule
import org.junit.Test

class RecorderBottomSheetTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verifyRecorderScreenContentIsDisplayed() {
        composeTestRule.setContent {
            VocalizeTheme {
                RecorderBottomSheet()
            }
        }

        composeTestRule.onNodeWithText("Recorder").assertIsDisplayed()
    }
}
