package com.nborba.vocalize.core.permission.model

/**
 * Text content displayed in permission rationale and settings dialogs.
 *
 * @property title The header text for the dialog.
 * @property description The explanatory body text detailing why the permission is required.
 */
data class PermissionDialogContent(
    val title: String,
    val description: String,
)
