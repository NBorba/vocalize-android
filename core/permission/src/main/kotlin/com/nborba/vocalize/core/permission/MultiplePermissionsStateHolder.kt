package com.nborba.vocalize.core.permission

import androidx.compose.runtime.Stable
import com.nborba.vocalize.core.permission.model.PermissionState

/**
 * State holder for observing and requesting multiple Android permissions simultaneously.
 */
@Stable
interface MultiplePermissionsStateHolder {
    /** List of requested manifest permissions. */
    val permissions: List<String>

    /** Map of each permission to its current [PermissionState]. */
    val permissionStates: Map<String, PermissionState>

    /** True if all requested permissions are currently [PermissionState.Granted]. */
    val allPermissionsGranted: Boolean

    /** True if any permission is currently in [PermissionState.ShowRationale]. */
    val shouldShowRationale: Boolean

    /** Triggers the system prompt for multiple permissions. */
    fun launchMultiplePermissionRequest()
}
