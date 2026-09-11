package com.nborba.vocalize.core.permission.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.nborba.vocalize.core.common.util.openAppSettings
import com.nborba.vocalize.core.designsystem.component.VocalizeTextButton
import com.nborba.vocalize.core.permission.R
import com.nborba.vocalize.core.permission.model.PermissionPromptContent
import com.nborba.vocalize.core.designsystem.R as DSR

/**
 * Material 3 dialog prompting the user to open System Settings for permanently denied permissions.
 *
 * @param content Permission prompt content containing title and description.
 * @param onDismiss Callback invoked when the dialog is dismissed.
 * @param onConfirm Callback invoked when the dialog is confirmed.
 */
@Composable
fun DefaultPermissionSettingsDialog(
    content: PermissionPromptContent,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = content.title) },
        text = { Text(text = content.description) },
        confirmButton = {
            VocalizeTextButton(
                text = stringResource(R.string.permission_settings_confirm_button),
                onClick = {
                    onConfirm()
                    context.openAppSettings()
                },
            )
        },
        dismissButton = {
            VocalizeTextButton(
                text = stringResource(DSR.string.button_cancel),
                onClick = onDismiss,
            )
        },
        modifier = modifier,
    )
}
