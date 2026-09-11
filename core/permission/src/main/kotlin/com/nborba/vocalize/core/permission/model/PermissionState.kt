package com.nborba.vocalize.core.permission.model

/**
 * Represents the current resolution state of a system permission.
 */
sealed interface PermissionState {
    /** Permission is granted and active. */
    data object Granted : PermissionState

    /** Permission is not granted, but can be requested directly. */
    data object Denied : PermissionState

    /** Permission is permanently denied; user must enable it via System Settings. */
    data object DeniedPermanently : PermissionState

    /** Permission was previously denied; a rationale should be shown before re-requesting. */
    data object ShowRationale : PermissionState
}
