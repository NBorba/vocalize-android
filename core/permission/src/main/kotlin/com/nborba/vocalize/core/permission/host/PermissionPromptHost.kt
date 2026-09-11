package com.nborba.vocalize.core.permission.host

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nborba.vocalize.core.permission.model.PermissionPromptContent
import com.nborba.vocalize.core.permission.ui.DefaultPermissionRationaleDialog
import com.nborba.vocalize.core.permission.ui.DefaultPermissionSettingsDialog

/**
 * Host composable that renders active rationale or settings prompts triggered by a [PermissionPromptHostState].
 *
 * @param hostState The [PermissionPromptHostState] managing active prompt data.
 * @param modifier Modifier applied to default dialog containers.
 * @param rationaleContent Default composable slot for rendering the rationale prompt.
 * @param settingsContent Default composable slot for rendering the settings prompt.
 */
@Composable
fun PermissionPromptHost(
    hostState: PermissionPromptHostState,
    modifier: Modifier = Modifier,
    rationaleContent: @Composable (
        content: PermissionPromptContent,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit,
    ) -> Unit = { content, onConfirm, onDismiss ->
        DefaultPermissionRationaleDialog(
            content = content,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            modifier = modifier,
        )
    },
    settingsContent: @Composable (
        content: PermissionPromptContent,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit,
    ) -> Unit = { content, onConfirm, onDismiss ->
        DefaultPermissionSettingsDialog(
            content = content,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            modifier = modifier,
        )
    },
) {
    val currentPrompt = hostState.currentPrompt ?: return

    when (currentPrompt) {
        is PermissionPrompt.Rationale -> {
            rationaleContent(
                currentPrompt.content,
                currentPrompt.onConfirm,
            ) { hostState.dismissPrompt() }
        }
        is PermissionPrompt.Settings -> {
            settingsContent(
                currentPrompt.content,
                currentPrompt.onConfirm,
            ) { hostState.dismissPrompt() }
        }
    }
}
