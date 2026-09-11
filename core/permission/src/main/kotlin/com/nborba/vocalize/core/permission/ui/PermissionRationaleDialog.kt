package com.nborba.vocalize.core.permission.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nborba.vocalize.core.designsystem.R
import com.nborba.vocalize.core.designsystem.component.VocalizeTextButton
import com.nborba.vocalize.core.permission.model.PermissionDialogContent

/**
 * Material 3 dialog displaying permission rationale text before re-requesting a permission.
 *
 * @param content Title and description text for the dialog.
 * @param onDismiss Callback invoked when the dialog is dismissed or canceled.
 * @param onConfirm Callback invoked when the user confirms and requests the permission.
 */
@Composable
fun PermissionRationaleDialog(
    content: PermissionDialogContent,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = content.title) },
        text = { Text(text = content.description) },
        confirmButton = {
            VocalizeTextButton(
                text = stringResource(R.string.button_allow),
                onClick = onConfirm,
            )
        },
        dismissButton = {
            VocalizeTextButton(
                text = stringResource(R.string.button_cancel),
                onClick = onDismiss,
            )
        },
        modifier = modifier,
    )
}
