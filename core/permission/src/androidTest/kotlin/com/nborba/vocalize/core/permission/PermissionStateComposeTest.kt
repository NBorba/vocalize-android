package com.nborba.vocalize.core.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.testing.TestLifecycleOwner
import com.nborba.vocalize.core.permission.model.PermissionState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class PermissionStateComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    companion object {
        private const val TEST_PERMISSION = Manifest.permission.RECORD_AUDIO
        private val TEST_PERMISSIONS =
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.POST_NOTIFICATIONS,
            )

        @JvmStatic
        @BeforeClass
        fun setupStaticMocks() {
            mockkStatic(ContextCompat::class, ActivityCompat::class)
        }

        @JvmStatic
        @AfterClass
        fun tearDownStaticMocks() {
            unmockkAll()
        }

        private fun mockPermissions(
            isGranted: Boolean,
            showRationale: Boolean = false,
        ) {
            every { ContextCompat.checkSelfPermission(any(), any()) } returns
                if (isGranted) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
            every { ActivityCompat.shouldShowRequestPermissionRationale(any(), any()) } returns showRationale
        }
    }

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun rememberPermissionState_returnsGranted_whenPermissionIsGranted() {
        assertPermissionStateResolution(
            isGranted = true,
            showRationale = false,
            expectedState = PermissionState.Granted,
        )
    }

    @Test
    fun rememberPermissionState_returnsShowRationale_whenRationaleShouldBeShown() {
        assertPermissionStateResolution(
            isGranted = false,
            showRationale = true,
            expectedState = PermissionState.ShowRationale,
        )
    }

    @Test
    fun rememberPermissionState_returnsDenied_whenFirstTimeDenied() {
        assertPermissionStateResolution(
            isGranted = false,
            showRationale = false,
            expectedState = PermissionState.Denied,
        )
    }

    @Test
    fun rememberPermissionState_returnsDeniedPermanently_afterRequestLaunchedWhenRationaleIsFalse() {
        mockPermissions(isGranted = false, showRationale = false)
        var stateHolder: PermissionStateHolder? = null

        composeTestRule.setContent {
            stateHolder = rememberPermissionState(TEST_PERMISSION)
        }

        assertEquals(PermissionState.Denied, stateHolder?.status)
        composeTestRule.runOnIdle { stateHolder?.launchPermissionRequest() }
        assertEquals(PermissionState.DeniedPermanently, stateHolder?.status)
    }

    @Test
    fun rememberPermissionState_updatesStatusToGranted_whenAppIsResumedAfterGrantingInSettings() {
        mockPermissions(isGranted = false, showRationale = false)
        val lifecycleOwner = TestLifecycleOwner(initialState = Lifecycle.State.RESUMED)
        var stateHolder: PermissionStateHolder? = null

        composeTestRule.setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                stateHolder = rememberPermissionState(TEST_PERMISSION)
            }
        }

        assertEquals(PermissionState.Denied, stateHolder?.status)

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        mockPermissions(isGranted = true)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        assertEquals(PermissionState.Granted, stateHolder?.status)
    }

    @Test
    fun rememberMultiplePermissionsState_returnsAllPermissionsGranted_whenAllGranted() {
        mockPermissions(isGranted = true)
        var multipleStateHolder: MultiplePermissionsStateHolder? = null

        composeTestRule.setContent {
            multipleStateHolder = rememberMultiplePermissionsState(TEST_PERMISSIONS)
        }

        assertTrue(multipleStateHolder?.allPermissionsGranted == true)
        assertFalse(multipleStateHolder?.shouldShowRationale == true)
        assertEquals(PermissionState.Granted, multipleStateHolder?.permissionStates?.get(TEST_PERMISSIONS[0]))
    }

    @Test
    fun rememberMultiplePermissionsState_returnsShouldShowRationale_whenAnyPermissionRequiresRationale() {
        val perm1 = TEST_PERMISSIONS[0]
        val perm2 = TEST_PERMISSIONS[1]

        every { ContextCompat.checkSelfPermission(any(), perm1) } returns PackageManager.PERMISSION_GRANTED
        every { ContextCompat.checkSelfPermission(any(), perm2) } returns PackageManager.PERMISSION_DENIED
        every { ActivityCompat.shouldShowRequestPermissionRationale(any(), perm2) } returns true

        var multipleStateHolder: MultiplePermissionsStateHolder? = null

        composeTestRule.setContent {
            multipleStateHolder = rememberMultiplePermissionsState(TEST_PERMISSIONS)
        }

        assertFalse(multipleStateHolder?.allPermissionsGranted == true)
        assertTrue(multipleStateHolder?.shouldShowRationale == true)
        assertEquals(PermissionState.Granted, multipleStateHolder?.permissionStates?.get(perm1))
        assertEquals(PermissionState.ShowRationale, multipleStateHolder?.permissionStates?.get(perm2))
    }

    @Test
    fun rememberMultiplePermissionsState_returnsDeniedPermanently_afterRequestLaunchedWhenRationaleIsFalse() {
        mockPermissions(isGranted = false, showRationale = false)
        var multipleStateHolder: MultiplePermissionsStateHolder? = null

        composeTestRule.setContent {
            multipleStateHolder = rememberMultiplePermissionsState(TEST_PERMISSIONS)
        }

        assertFalse(multipleStateHolder?.allPermissionsGranted == true)

        composeTestRule.runOnIdle { multipleStateHolder?.launchMultiplePermissionRequest() }

        assertEquals(PermissionState.DeniedPermanently, multipleStateHolder?.permissionStates?.get(TEST_PERMISSIONS[0]))
        assertEquals(PermissionState.DeniedPermanently, multipleStateHolder?.permissionStates?.get(TEST_PERMISSIONS[1]))
    }

    @Test
    fun rememberMultiplePermissionsState_updatesStatusOnResume_whenPermissionsGrantedInSettings() {
        mockPermissions(isGranted = false)
        val lifecycleOwner = TestLifecycleOwner(initialState = Lifecycle.State.RESUMED)
        var multipleStateHolder: MultiplePermissionsStateHolder? = null

        composeTestRule.setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                multipleStateHolder = rememberMultiplePermissionsState(TEST_PERMISSIONS)
            }
        }

        assertFalse(multipleStateHolder?.allPermissionsGranted == true)

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        mockPermissions(isGranted = true)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        assertTrue(multipleStateHolder?.allPermissionsGranted == true)
    }

    private fun assertPermissionStateResolution(
        isGranted: Boolean,
        showRationale: Boolean,
        expectedState: PermissionState,
    ) {
        mockPermissions(isGranted = isGranted, showRationale = showRationale)
        var stateHolder: PermissionStateHolder? = null

        composeTestRule.setContent {
            stateHolder = rememberPermissionState(TEST_PERMISSION)
        }

        assertEquals(expectedState, stateHolder?.status)
    }
}
