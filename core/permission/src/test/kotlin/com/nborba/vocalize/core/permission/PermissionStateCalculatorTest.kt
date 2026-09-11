package com.nborba.vocalize.core.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nborba.vocalize.core.permission.model.PermissionState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class PermissionStateCalculatorTest {
    private val context: Context = mockk()
    private val activity: Activity = mockk()
    private val testPermission = "android.permission.RECORD_AUDIO"

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupStaticMocks() {
            mockkStatic(ContextCompat::class, ActivityCompat::class)
        }

        @JvmStatic
        @AfterAll
        fun tearDownStaticMocks() {
            unmockkAll()
        }

        @JvmStatic
        fun permissionCalculationTestCases() =
            listOf(
                Arguments.of(true, false, false, PermissionState.Granted),
                Arguments.of(false, true, true, PermissionState.ShowRationale),
                Arguments.of(false, false, true, PermissionState.DeniedPermanently),
                Arguments.of(false, false, false, PermissionState.Denied),
            )
    }

    @AfterEach
    fun tearDown() = clearAllMocks()

    @Nested
    inner class SinglePermissionCalculationTests {
        @ParameterizedTest
        @MethodSource(
            "com.nborba.vocalize.core.permission.PermissionStateCalculatorTest#permissionCalculationTestCases",
        )
        fun `calculatePermissionState resolves expected state`(
            isGranted: Boolean,
            showRationale: Boolean,
            hasRequestedBefore: Boolean,
            expectedState: PermissionState,
        ) {
            every {
                ContextCompat.checkSelfPermission(context, testPermission)
            } returns if (isGranted) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
            every {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, testPermission)
            } returns showRationale

            val state =
                calculatePermissionState(
                    context = context,
                    activity = activity,
                    permission = testPermission,
                    hasRequestedBefore = hasRequestedBefore,
                )

            assertEquals(expectedState, state)
        }
    }

    @Nested
    inner class MultiplePermissionsCalculationTests {
        @Test
        fun `calculateMultiplePermissionsState correctly maps states for multiple permissions`() {
            val permission1 = "android.permission.RECORD_AUDIO"
            val permission2 = "android.permission.POST_NOTIFICATIONS"
            val permissions = listOf(permission1, permission2)

            every {
                ContextCompat.checkSelfPermission(context, permission1)
            } returns PackageManager.PERMISSION_GRANTED
            every {
                ContextCompat.checkSelfPermission(context, permission2)
            } returns PackageManager.PERMISSION_DENIED
            every {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission2)
            } returns true

            val states =
                calculateMultiplePermissionsState(
                    context = context,
                    activity = activity,
                    permissions = permissions,
                    hasRequestedBefore = true,
                )

            assertEquals(PermissionState.Granted, states[permission1])
            assertEquals(PermissionState.ShowRationale, states[permission2])
        }
    }
}
