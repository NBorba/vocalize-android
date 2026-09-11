package com.nborba.vocalize.core.permission.host

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.nborba.vocalize.core.common.util.findActivity
import com.nborba.vocalize.core.common.util.openAppSettings
import com.nborba.vocalize.core.permission.model.PermissionPromptContent
import com.nborba.vocalize.core.permission.util.hasPermission
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Result returned by [PermissionPromptHostState.requestPermission] and [PermissionPromptHostState.requestPermissions].
 */
enum class PermissionResult {
    /** All permissions required were granted. */
    Granted,

    /** Permission request was denied or dismissed by the user. */
    Denied,

    /** Permission is permanently denied; user was prompted with System Settings. */
    DeniedPermanently,
}

/**
 * Represents active prompt data hosted by [PermissionPromptHost].
 */
sealed interface PermissionPrompt {
    val content: PermissionPromptContent

    data class Rationale(
        override val content: PermissionPromptContent,
        val onConfirm: () -> Unit,
        val onDismiss: () -> Unit,
    ) : PermissionPrompt

    data class Settings(
        override val content: PermissionPromptContent,
        val onConfirm: () -> Unit,
        val onDismiss: () -> Unit,
    ) : PermissionPrompt
}

/**
 * State holder that manages permission requests and hosts active permission prompts,
 * operating identically to Compose [androidx.compose.material3.SnackbarHostState].
 */
@Stable
class PermissionPromptHostState internal constructor() {
    var currentPrompt by mutableStateOf<PermissionPrompt?>(null)
        internal set

    internal lateinit var context: Context
    internal var activity: Activity? = null
    internal lateinit var systemLauncher: (permissions: List<String>, callback: (Map<String, Boolean>) -> Unit) -> Unit

    private val hasRequestedBeforeMap = mutableMapOf<String, Boolean>()

    /**
     * Requests a single permission and suspends until a [PermissionResult] is returned.
     *
     * @param permission Manifest permission string (e.g. `Manifest.permission.RECORD_AUDIO`).
     * @param rationaleContent Optional custom rationale prompt text content.
     * @param settingsContent Optional custom settings prompt text content.
     * @return A [PermissionResult] indicating request outcome.
     */
    suspend fun requestPermission(
        permission: String,
        rationaleContent: PermissionPromptContent? = null,
        settingsContent: PermissionPromptContent? = null,
    ): PermissionResult =
        requestPermissions(
            permissions = listOf(permission),
            rationaleContent = rationaleContent,
            settingsContent = settingsContent,
        )

    /**
     * Requests multiple permissions and suspends until a [PermissionResult] is returned.
     *
     * @param permissions List of manifest permission strings.
     * @param rationaleContent Optional custom rationale prompt text content.
     * @param settingsContent Optional custom settings prompt text content.
     * @return A [PermissionResult] indicating request outcome.
     */
    suspend fun requestPermissions(
        permissions: List<String>,
        rationaleContent: PermissionPromptContent? = null,
        settingsContent: PermissionPromptContent? = null,
    ): PermissionResult {
        // 1. Return immediately if all permissions are already granted
        if (permissions.all { context.hasPermission(it) }) {
            dismissPrompt()
            return PermissionResult.Granted
        }

        val ratContent = rationaleContent ?: createDefaultRationaleContent(permissions)
        val setContent = settingsContent ?: createDefaultSettingsContent(permissions)

        // 2. Show rationale prompt if required before launching request
        if (shouldShowRationale(permissions)) {
            val confirmed = showRationalePrompt(ratContent)
            if (!confirmed) return PermissionResult.Denied
        }

        // 3. Show settings prompt if previously permanently denied
        if (isPermanentlyDeniedBeforeLaunch(permissions)) {
            return showSettingsPrompt(setContent)
        }

        // 4. Launch system permission request prompt
        val result = launchSystemRequest(permissions)
        if (result.values.all { it }) {
            return PermissionResult.Granted
        }

        // 5. Show settings prompt if system launcher suppressed prompt due to permanent denial
        if (isPermanentlyDeniedAfterLaunch(permissions)) {
            return showSettingsPrompt(setContent)
        }

        return PermissionResult.Denied
    }

    /**
     * Dismisses any active rationale or settings prompt.
     */
    fun dismissPrompt() {
        currentPrompt = null
    }

    private fun shouldShowRationale(permissions: List<String>): Boolean =
        activity?.let { act ->
            permissions.any { perm ->
                ActivityCompat.shouldShowRequestPermissionRationale(act, perm)
            }
        } ?: false

    private fun isPermanentlyDeniedBeforeLaunch(permissions: List<String>): Boolean =
        permissions.any { perm ->
            val requestedBefore = hasRequestedBeforeMap[perm] ?: false
            val showRationale =
                activity?.let { act ->
                    ActivityCompat.shouldShowRequestPermissionRationale(act, perm)
                } ?: false
            !context.hasPermission(perm) && requestedBefore && !showRationale
        }

    private fun isPermanentlyDeniedAfterLaunch(permissions: List<String>): Boolean =
        permissions.any { perm ->
            val showRationale =
                activity?.let { act ->
                    ActivityCompat.shouldShowRequestPermissionRationale(act, perm)
                } ?: false
            !context.hasPermission(perm) && !showRationale
        }

    private suspend fun showRationalePrompt(content: PermissionPromptContent): Boolean =
        suspendCancellableCoroutine { continuation ->
            currentPrompt =
                PermissionPrompt.Rationale(
                    content = content,
                    onConfirm = {
                        dismissPrompt()
                        continuation.resume(true)
                    },
                    onDismiss = {
                        dismissPrompt()
                        continuation.resume(false)
                    },
                )
        }

    private suspend fun showSettingsPrompt(content: PermissionPromptContent): PermissionResult {
        suspendCancellableCoroutine { continuation ->
            currentPrompt =
                PermissionPrompt.Settings(
                    content = content,
                    onConfirm = {
                        dismissPrompt()
                        context.openAppSettings()
                        continuation.resume(Unit)
                    },
                    onDismiss = {
                        dismissPrompt()
                        continuation.resume(Unit)
                    },
                )
        }
        return PermissionResult.DeniedPermanently
    }

    private suspend fun launchSystemRequest(permissions: List<String>): Map<String, Boolean> =
        suspendCancellableCoroutine { continuation ->
            systemLauncher(permissions) { result ->
                permissions.forEach { hasRequestedBeforeMap[it] = true }
                continuation.resume(result)
            }
        }

    private fun createDefaultRationaleContent(permissions: List<String>): PermissionPromptContent {
        val name = formatPermissionNames(permissions)
        return PermissionPromptContent(
            title = "$name Permission Required",
            description = "This feature requires $name permission to function properly.",
        )
    }

    private fun createDefaultSettingsContent(permissions: List<String>): PermissionPromptContent {
        val name = formatPermissionNames(permissions)
        return PermissionPromptContent(
            title = "$name Permission Denied",
            description = "$name permission is permanently disabled. Please enable $name in System Settings.",
        )
    }

    private fun formatPermissionNames(permissions: List<String>): String =
        permissions.joinToString(", ") { perm ->
            perm
                .substringAfterLast(".")
                .removePrefix("PERMISSION_")
                .replace("_", " ")
                .lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}

/**
 * Remembers a [PermissionPromptHostState] bound to a permission prompt host.
 */
@Composable
fun rememberPermissionPromptHostState(): PermissionPromptHostState {
    val context: Context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    val hostState = remember { PermissionPromptHostState() }

    var pendingContinuation by remember {
        mutableStateOf<((Map<String, Boolean>) -> Unit)?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { result ->
            pendingContinuation?.invoke(result)
            pendingContinuation = null
        }

    hostState.context = context
    hostState.activity = activity
    hostState.systemLauncher = { permissions, callback ->
        pendingContinuation = callback
        launcher.launch(permissions.toTypedArray())
    }

    return hostState
}
