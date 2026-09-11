package com.nborba.vocalize.core.permission.host

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nborba.vocalize.core.common.util.MainDispatcherExtension
import com.nborba.vocalize.core.common.util.openAppSettings
import com.nborba.vocalize.core.permission.model.PermissionPromptContent
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
internal class PermissionPromptHostStateTest {
    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val context: Context = mockk(relaxed = true)
    private val activity: Activity = mockk(relaxed = true)
    private lateinit var hostState: PermissionPromptHostState
    private var launcherCallback: ((Map<String, Boolean>) -> Unit)? = null

    @BeforeEach
    fun setUp() {
        mockkStatic(ContextCompat::class)
        mockkStatic(ActivityCompat::class)
        mockkStatic("com.nborba.vocalize.core.common.util.ContextExtensionsKt")

        every { context.openAppSettings() } just runs
        every { context.getString(any(), *anyVararg()) } returns "Permission Text"
        every { ContextCompat.checkSelfPermission(context, any()) } returns PackageManager.PERMISSION_DENIED
        every { ActivityCompat.shouldShowRequestPermissionRationale(activity, any()) } returns false

        hostState =
            PermissionPromptHostState().apply {
                this.context = this@PermissionPromptHostStateTest.context
                this.activity = this@PermissionPromptHostStateTest.activity
                this.systemLauncher = { _, callback -> launcherCallback = callback }
            }
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ContextCompat::class)
        unmockkStatic(ActivityCompat::class)
        unmockkStatic("com.nborba.vocalize.core.common.util.ContextExtensionsKt")
    }

    @Test
    fun `requestPermission returns Granted immediately if permission is already granted`() =
        runTest {
            every { ContextCompat.checkSelfPermission(context, PERMISSION) } returns PackageManager.PERMISSION_GRANTED

            val result = hostState.requestPermission(PERMISSION)

            assertEquals(PermissionResult.Granted, result)
            assertNull(hostState.currentPrompt)
        }

    @Test
    fun `requestPermissions returns Granted when all requested permissions are granted`() =
        runTest {
            every { ContextCompat.checkSelfPermission(context, any()) } returns PackageManager.PERMISSION_GRANTED

            val result = hostState.requestPermissions(listOf(PERMISSION, SECOND_PERMISSION))

            assertEquals(PermissionResult.Granted, result)
            assertNull(hostState.currentPrompt)
        }

    @Test
    fun `when custom rationaleContent is provided, displays custom rationale content in prompt`() =
        runTest(mainDispatcherExtension.testDispatcher) {
            every { ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION) } returns true

            val customContent = PermissionPromptContent("Custom Title", "Custom Description")
            val job = launch { hostState.requestPermission(PERMISSION, rationaleContent = customContent) }

            val prompt = hostState.currentPrompt as PermissionPrompt.Rationale
            assertEquals(customContent, prompt.content)

            job.cancel()
        }

    @Test
    fun `when rationale is needed and user confirms, launches system request and handles grant`() =
        runTest(mainDispatcherExtension.testDispatcher) {
            every { ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION) } returns true

            var result: PermissionResult? = null
            val job = launch { result = hostState.requestPermission(PERMISSION) }

            (hostState.currentPrompt as PermissionPrompt.Rationale).onConfirm()

            every { ContextCompat.checkSelfPermission(context, PERMISSION) } returns PackageManager.PERMISSION_GRANTED
            launcherCallback?.invoke(mapOf(PERMISSION to true))

            assertEquals(PermissionResult.Granted, result)
            assertNull(hostState.currentPrompt)

            job.cancel()
        }

    @Test
    fun `when rationale is needed and user dismisses, returns Denied without launching system request`() =
        runTest(mainDispatcherExtension.testDispatcher) {
            every { ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION) } returns true

            var result: PermissionResult? = null
            val job = launch { result = hostState.requestPermission(PERMISSION) }

            (hostState.currentPrompt as PermissionPrompt.Rationale).onDismiss()

            assertEquals(PermissionResult.Denied, result)
            assertNull(hostState.currentPrompt)

            job.cancel()
        }

    @Test
    fun `when request is denied without rationale or permanent denial, returns Denied`() =
        runTest(mainDispatcherExtension.testDispatcher) {
            var result: PermissionResult? = null
            val job = launch { result = hostState.requestPermission(PERMISSION) }

            every { ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION) } returns true
            launcherCallback?.invoke(mapOf(PERMISSION to false))

            assertEquals(PermissionResult.Denied, result)
            assertNull(hostState.currentPrompt)

            job.cancel()
        }

    @Test
    fun `when request is permanently denied after launch, confirms Settings prompt to open app settings`() =
        runTest(mainDispatcherExtension.testDispatcher) {
            var result: PermissionResult? = null
            val job = launch { result = hostState.requestPermission(PERMISSION) }

            launcherCallback?.invoke(mapOf(PERMISSION to false))

            (hostState.currentPrompt as PermissionPrompt.Settings).onConfirm()

            verify { context.openAppSettings() }
            assertEquals(PermissionResult.DeniedPermanently, result)
            assertNull(hostState.currentPrompt)

            job.cancel()
        }

    @Test
    fun `when settings prompt is dismissed, returns DeniedPermanently without opening app settings`() =
        runTest(mainDispatcherExtension.testDispatcher) {
            var result: PermissionResult? = null
            val job = launch { result = hostState.requestPermission(PERMISSION) }

            launcherCallback?.invoke(mapOf(PERMISSION to false))

            (hostState.currentPrompt as PermissionPrompt.Settings).onDismiss()

            assertEquals(PermissionResult.DeniedPermanently, result)
            assertNull(hostState.currentPrompt)

            job.cancel()
        }

    @Test
    fun `when permission previously permanently denied before launch, skips system launcher and shows Settings UI`() =
        runTest(mainDispatcherExtension.testDispatcher) {
            var callCount = 0
            hostState.systemLauncher = { _, callback ->
                callCount++
                callback(mapOf(PERMISSION to false))
            }

            val job1 = launch { hostState.requestPermission(PERMISSION) }
            (hostState.currentPrompt as? PermissionPrompt.Settings)?.onDismiss()
            job1.cancel()

            assertEquals(1, callCount)

            var result2: PermissionResult? = null
            val job2 = launch { result2 = hostState.requestPermission(PERMISSION) }

            assertEquals(1, callCount)
            (hostState.currentPrompt as PermissionPrompt.Settings).onDismiss()

            assertEquals(PermissionResult.DeniedPermanently, result2)
            job2.cancel()
        }

    @Test
    fun `dismissPrompt clears active prompt`() {
        hostState.currentPrompt = PermissionPrompt.Rationale(mockk(), {}, {})

        hostState.dismissPrompt()

        assertNull(hostState.currentPrompt)
    }

    private companion object {
        const val PERMISSION = "android.permission.RECORD_AUDIO"
        const val SECOND_PERMISSION = "android.permission.CAMERA"
    }
}
