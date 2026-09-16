package com.nborba.vocalize.core.permission.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import com.nborba.vocalize.core.permission.model.PermissionPromptContent
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DefaultPermissionRationaleDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysTitleAndDescriptionAndButtons() {
        val content =
            PermissionPromptContent(
                title = "Microphone Permission Required",
                description = "We need microphone access to record audio notes.",
            )

        composeTestRule.setContent {
            VocalizeTheme {
                DefaultPermissionRationaleDialog(
                    content = content,
                    onDismiss = {},
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Microphone Permission Required").assertIsDisplayed()
        composeTestRule.onNodeWithText("We need microphone access to record audio notes.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Allow").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun clickingAllowButton_triggersOnConfirm() {
        var confirmClicked = false
        val content = PermissionPromptContent(title = "Title", description = "Description")

        composeTestRule.setContent {
            VocalizeTheme {
                DefaultPermissionRationaleDialog(
                    content = content,
                    onDismiss = {},
                    onConfirm = { confirmClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithText("Allow").performClick()

        assertTrue("Expected onConfirm callback to be invoked", confirmClicked)
    }

    @Test
    fun clickingCancelButton_triggersOnDismiss() {
        var dismissClicked = false
        val content = PermissionPromptContent(title = "Title", description = "Description")

        composeTestRule.setContent {
            VocalizeTheme {
                DefaultPermissionRationaleDialog(
                    content = content,
                    onDismiss = { dismissClicked = true },
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Cancel").performClick()

        assertTrue("Expected onDismiss callback to be invoked", dismissClicked)
    }
}
