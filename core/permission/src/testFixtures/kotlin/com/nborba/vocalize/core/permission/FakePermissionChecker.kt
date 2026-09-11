package com.nborba.vocalize.core.permission

import com.nborba.vocalize.core.permission.domain.PermissionChecker

/**
 * Fake implementation of [PermissionChecker] for unit testing.
 */
class FakePermissionChecker(
    private val defaultGranted: Boolean = true,
    private val grantedPermissions: MutableSet<String> = mutableSetOf(),
) : PermissionChecker {
    /**
     * Sets whether a specific [permission] should be reported as granted.
     */
    fun setPermissionGranted(
        permission: String,
        isGranted: Boolean,
    ) {
        if (isGranted) {
            grantedPermissions.add(permission)
        } else {
            grantedPermissions.remove(permission)
        }
    }

    override fun hasPermission(permission: String): Boolean =
        if (grantedPermissions.isEmpty()) defaultGranted else grantedPermissions.contains(permission)

    override fun hasAllPermissions(permissions: List<String>): Boolean = permissions.all { hasPermission(it) }
}
