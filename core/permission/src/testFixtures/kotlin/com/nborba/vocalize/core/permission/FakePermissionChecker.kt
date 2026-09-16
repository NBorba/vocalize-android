package com.nborba.vocalize.core.permission

import com.nborba.vocalize.core.permission.domain.PermissionChecker

/**
 * Fake implementation of [PermissionChecker] for unit testing.
 */
class FakePermissionChecker(
    private val defaultGranted: Boolean = true,
    private val permissionStates: MutableMap<String, Boolean> = mutableMapOf(),
) : PermissionChecker {
    /**
     * Sets whether a specific [permission] should be reported as granted.
     */
    fun setPermissionGranted(
        permission: String,
        isGranted: Boolean,
    ) {
        permissionStates[permission] = isGranted
    }

    override fun hasPermission(permission: String): Boolean = permissionStates[permission] ?: defaultGranted

    override fun hasAllPermissions(permissions: List<String>): Boolean = permissions.all { hasPermission(it) }
}
