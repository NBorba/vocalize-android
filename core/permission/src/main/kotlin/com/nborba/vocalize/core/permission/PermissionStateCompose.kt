package com.nborba.vocalize.core.permission

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.nborba.vocalize.core.common.util.findActivity
import com.nborba.vocalize.core.permission.model.PermissionState
import com.nborba.vocalize.core.permission.util.hasPermission

/**
 * Remembers and tracks the state of a single permission across compositions and lifecycle events.
 *
 * @param permission The manifest permission string to request and observe.
 * @return A [PermissionStateHolder] providing current status and request triggers.
 */
@Composable
fun rememberPermissionState(permission: String): PermissionStateHolder {
    val context: Context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    var hasRequestedBefore by rememberSaveable { mutableStateOf(false) }

    fun calculatePermissionState(): PermissionState =
        calculatePermissionState(
            context = context,
            activity = activity,
            permission = permission,
            hasRequestedBefore = hasRequestedBefore,
        )

    var permissionState by remember(permission) { mutableStateOf(calculatePermissionState()) }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { _ ->
            hasRequestedBefore = true
            permissionState = calculatePermissionState()
        }

    LifecycleResumeEffect(permission) {
        permissionState = calculatePermissionState()
        onPauseOrDispose { }
    }

    return remember(permission, permissionState) {
        object : PermissionStateHolder {
            override val permission: String = permission
            override val status: PermissionState = permissionState

            override fun launchPermissionRequest() {
                hasRequestedBefore = true
                launcher.launch(permission)
            }
        }
    }
}

/**
 * Remembers and tracks the aggregate state of multiple permissions across compositions and lifecycle events.
 *
 * @param permissions List of manifest permission strings to request and observe.
 * @return A [MultiplePermissionsStateHolder] providing aggregate status and request triggers.
 */
@Composable
fun rememberMultiplePermissionsState(permissions: List<String>): MultiplePermissionsStateHolder {
    val context: Context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    var hasRequestedBefore by rememberSaveable { mutableStateOf(false) }

    fun calculatePermissionStates(): Map<String, PermissionState> =
        calculateMultiplePermissionsState(
            context = context,
            activity = activity,
            permissions = permissions,
            hasRequestedBefore = hasRequestedBefore,
        )

    var permissionStates by remember(permissions) {
        mutableStateOf(calculatePermissionStates())
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { _ ->
            hasRequestedBefore = true
            permissionStates = calculatePermissionStates()
        }

    LifecycleResumeEffect(permissions) {
        permissionStates = calculatePermissionStates()
        onPauseOrDispose { }
    }

    return remember(permissions, permissionStates) {
        object : MultiplePermissionsStateHolder {
            override val permissions: List<String> = permissions
            override val permissionStates: Map<String, PermissionState> = permissionStates

            override val allPermissionsGranted: Boolean
                get() = permissionStates.values.all { it is PermissionState.Granted }

            override val shouldShowRationale: Boolean
                get() = permissionStates.values.any { it is PermissionState.ShowRationale }

            override fun launchMultiplePermissionRequest() {
                hasRequestedBefore = true
                launcher.launch(permissions.toTypedArray())
            }
        }
    }
}

internal fun calculatePermissionState(
    context: Context,
    activity: Activity?,
    permission: String,
    hasRequestedBefore: Boolean,
): PermissionState {
    if (context.hasPermission(permission)) return PermissionState.Granted

    val showRationale =
        activity?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
        } ?: false

    return when {
        showRationale -> PermissionState.ShowRationale
        hasRequestedBefore -> PermissionState.DeniedPermanently
        else -> PermissionState.Denied
    }
}

internal fun calculateMultiplePermissionsState(
    context: Context,
    activity: Activity?,
    permissions: List<String>,
    hasRequestedBefore: Boolean,
): Map<String, PermissionState> =
    permissions.associateWith { permission ->
        calculatePermissionState(
            context = context,
            activity = activity,
            permission = permission,
            hasRequestedBefore = hasRequestedBefore,
        )
    }
