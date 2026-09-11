package com.nborba.vocalize.core.permission.data

import android.content.Context
import com.nborba.vocalize.core.permission.domain.PermissionChecker
import com.nborba.vocalize.core.permission.util.hasPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android implementation of [PermissionChecker] using [Context].
 */
@Singleton
internal class AndroidPermissionChecker
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : PermissionChecker {
        override fun hasPermission(permission: String): Boolean = context.hasPermission(permission)

        override fun hasAllPermissions(permissions: List<String>): Boolean = permissions.all { hasPermission(it) }
    }
