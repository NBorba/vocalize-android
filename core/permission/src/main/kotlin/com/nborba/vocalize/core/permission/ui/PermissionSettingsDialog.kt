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
import com.nborba.vocalize.core.permission.model.PermissionDialogContent
import com.nborba.vocalize.core.designsystem.R as DSR

/**
 * Material 3 dialog prompting the user to open System Settings for permanently denied permissions.
 *
 * @param content Title and description text for the dialog.
 * @param onDismiss Callback invoked when the dialog is dismissed.
 * @param onOpenSettings Optional callback invoked prior to launching system settings.
 */
@Composable
fun PermissionSettingsDialog(
    content: PermissionDialogContent,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = content.title) },
        text = { Text(text = content.description) },
        confirmButton = {
            VocalizeTextButton(
                text = stringResource(R.string.settings_dialog_confirm_cta),
                onClick = {
                    onOpenSettings()
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
