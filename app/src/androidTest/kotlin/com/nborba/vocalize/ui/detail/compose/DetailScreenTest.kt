package com.nborba.vocalize.ui.detail.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.nborba.vocalize.DetailUiStateFixture
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verifyDetailScreenContentIsDisplayed() {
        composeTestRule.setContent {
            VocalizeTheme {
                DetailContent(
                    uiState = DetailUiStateFixture.default(),
                    onUpClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Detail #1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Viewing Detail").assertIsDisplayed()
        composeTestRule.onNodeWithText("Go back").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun clickingTopBarBackArrow_triggersUpCallback() {
        var upClicked = false

        composeTestRule.setContent {
            VocalizeTheme {
                DetailContent(
                    uiState = DetailUiStateFixture.default(),
                    onUpClick = { upClicked = true },
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        assertTrue("Expected onUpClick callback to be invoked", upClicked)
    }

    @Test
    fun clickingGoBackButton_triggersBackCallback() {
        var backClicked = false

        composeTestRule.setContent {
            VocalizeTheme {
                DetailContent(
                    uiState = DetailUiStateFixture.default(),
                    onUpClick = {},
                    onBackClick = { backClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithText("Go back").performClick()

        assertTrue("Expected onBackClick callback to be invoked", backClicked)
    }
}
