package com.nborba.vocalize.core.permission

import com.nborba.vocalize.core.permission.model.PermissionState

/**
 * Fake implementation of [PermissionStateHolder] for Compose `@Preview`s and UI testing.
 */
class FakePermissionStateHolder(
    override val permission: String = "android.permission.RECORD_AUDIO",
    override var status: PermissionState = PermissionState.Granted,
    private val onRequestLaunched: () -> Unit = {},
) : PermissionStateHolder {
    var launchCount = 0
        private set

    override fun launchPermissionRequest() {
        launchCount++
        onRequestLaunched()
    }
}
