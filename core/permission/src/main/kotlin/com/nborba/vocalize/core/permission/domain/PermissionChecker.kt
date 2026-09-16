package com.nborba.vocalize.core.permission.domain

/**
 * Interface for checking system permission status
 */
interface PermissionChecker {
    /**
     * Returns true if the specified [permission] is currently granted.
     */
    fun hasPermission(permission: String): Boolean

    /**
     * Returns true if all specified [permissions] are currently granted.
     */
    fun hasAllPermissions(permissions: List<String>): Boolean
}
