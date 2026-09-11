package com.nborba.vocalize.core.permission.model

import androidx.compose.runtime.Composable

/**
 * Defines a feature's permission requirements and associated rationale/settings dialog content.
 */
sealed interface PermissionFeature {
    /** List of manifest permissions required by this feature. */
    val permissions: List<String>

    /** Rationale dialog content explaining feature permission requirements. */
    @get:Composable
    val rationaleContent: PermissionDialogContent

    /** Settings dialog content explaining how to enable permanently denied permissions. */
    @get:Composable
    val settingsContent: PermissionDialogContent
}
