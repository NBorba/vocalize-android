package com.nborba.vocalize.core.permission.host

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.nborba.vocalize.core.designsystem.theme.VocalizeTheme
import com.nborba.vocalize.core.permission.model.PermissionPromptContent
import org.junit.Rule
import org.junit.Test

class PermissionPromptHostTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenRationalePromptActive_displaysRationaleContent() {
        val hostState =
            PermissionPromptHostState().apply {
                currentPrompt =
                    PermissionPrompt.Rationale(
                        content =
                            PermissionPromptContent(
                                title = "Rationale Title",
                                description = "Rationale Description",
                            ),
                        onConfirm = {},
                        onDismiss = {},
                    )
            }

        composeTestRule.setContent {
            VocalizeTheme {
                PermissionPromptHost(hostState = hostState)
            }
        }

        composeTestRule.onNodeWithText("Rationale Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rationale Description").assertIsDisplayed()
    }

    @Test
    fun whenSettingsPromptActive_displaysSettingsContent() {
        val hostState =
            PermissionPromptHostState().apply {
                currentPrompt =
                    PermissionPrompt.Settings(
                        content =
                            PermissionPromptContent(
                                title = "Settings Title",
                                description = "Settings Description",
                            ),
                        onConfirm = {},
                        onDismiss = {},
                    )
            }

        composeTestRule.setContent {
            VocalizeTheme {
                PermissionPromptHost(hostState = hostState)
            }
        }

        composeTestRule.onNodeWithText("Settings Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Settings Description").assertIsDisplayed()
    }

    @Test
    fun whenCustomRationaleSlotProvided_rendersCustomSlot() {
        val hostState =
            PermissionPromptHostState().apply {
                currentPrompt =
                    PermissionPrompt.Rationale(
                        content =
                            PermissionPromptContent(
                                title = "Title",
                                description = "Description",
                            ),
                        onConfirm = {},
                        onDismiss = {},
                    )
            }

        composeTestRule.setContent {
            VocalizeTheme {
                PermissionPromptHost(
                    hostState = hostState,
                    rationaleContent = { content, _, _ ->
                        Text(text = "Custom Rationale Slot: ${content.title}")
                    },
                )
            }
        }

        composeTestRule.onNodeWithText("Custom Rationale Slot: Title").assertIsDisplayed()
    }

    @Test
    fun whenCustomSettingsSlotProvided_rendersCustomSlot() {
        val hostState =
            PermissionPromptHostState().apply {
                currentPrompt =
                    PermissionPrompt.Settings(
                        content =
                            PermissionPromptContent(
                                title = "Title",
                                description = "Description",
                            ),
                        onConfirm = {},
                        onDismiss = {},
                    )
            }

        composeTestRule.setContent {
            VocalizeTheme {
                PermissionPromptHost(
                    hostState = hostState,
                    settingsContent = { content, _, _ ->
                        Text(text = "Custom Settings Slot: ${content.title}")
                    },
                )
            }
        }

        composeTestRule.onNodeWithText("Custom Settings Slot: Title").assertIsDisplayed()
    }
}
