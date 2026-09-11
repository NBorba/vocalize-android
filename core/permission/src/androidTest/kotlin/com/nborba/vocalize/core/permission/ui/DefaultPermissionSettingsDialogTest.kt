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

class DefaultPermissionSettingsDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysTitleAndDescriptionAndButtons() {
        val content =
            PermissionPromptContent(
                title = "Microphone Permission Denied",
                description = "Permission is permanently disabled. Please enable it in Settings.",
            )

        composeTestRule.setContent {
            VocalizeTheme {
                DefaultPermissionSettingsDialog(
                    content = content,
                    onDismiss = {},
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Microphone Permission Denied").assertIsDisplayed()
        composeTestRule
            .onNodeWithText(
                "Permission is permanently disabled. Please enable it in Settings.",
            ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Open Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun clickingCancelButton_triggersOnDismiss() {
        var dismissClicked = false
        val content = PermissionPromptContent(title = "Title", description = "Description")

        composeTestRule.setContent {
            VocalizeTheme {
                DefaultPermissionSettingsDialog(
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
