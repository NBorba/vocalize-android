package com.nborba.vocalize.core.permission

import com.nborba.vocalize.core.permission.model.PermissionState

/**
 * Fake implementation of [MultiplePermissionsStateHolder] for Compose `@Preview`s and UI testing.
 */
class FakeMultiplePermissionsStateHolder(
    override val permissions: List<String> = listOf("android.permission.RECORD_AUDIO"),
    override var permissionStates: Map<String, PermissionState> = permissions.associateWith { PermissionState.Granted },
    private val onRequestLaunched: () -> Unit = {},
) : MultiplePermissionsStateHolder {
    var launchCount = 0
        private set

    override val allPermissionsGranted: Boolean
        get() = permissionStates.values.all { it is PermissionState.Granted }

    override val shouldShowRationale: Boolean
        get() = permissionStates.values.any { it is PermissionState.ShowRationale }

    override fun launchMultiplePermissionRequest() {
        launchCount++
        onRequestLaunched()
    }
}
