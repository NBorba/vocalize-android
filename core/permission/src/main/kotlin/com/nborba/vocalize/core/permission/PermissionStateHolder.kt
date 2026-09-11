package com.nborba.vocalize.core.permission

import androidx.compose.runtime.Stable
import com.nborba.vocalize.core.permission.model.PermissionState

/**
 * State holder for observing and requesting a single Android permission.
 */
@Stable
interface PermissionStateHolder {
    /** The manifest permission string (e.g., `Manifest.permission.RECORD_AUDIO`). */
    val permission: String

    /** The current [PermissionState] resolution. */
    val status: PermissionState

    /** Triggers the system permission request prompt. */
    fun launchPermissionRequest()
}
