package com.nborba.vocalize.core.permission.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AndroidPermissionCheckerTest {
    private val context: Context = mockk()
    private lateinit var permissionChecker: AndroidPermissionChecker

    @BeforeEach
    fun setUp() {
        mockkStatic(ContextCompat::class)
        permissionChecker = AndroidPermissionChecker(context)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ContextCompat::class)
    }

    @Test
    fun `hasPermission returns true when permission is granted`() {
        every { ContextCompat.checkSelfPermission(context, PERMISSION) } returns PackageManager.PERMISSION_GRANTED

        assertTrue(permissionChecker.hasPermission(PERMISSION))
    }

    @Test
    fun `hasPermission returns false when permission is denied`() {
        every { ContextCompat.checkSelfPermission(context, PERMISSION) } returns PackageManager.PERMISSION_DENIED

        assertFalse(permissionChecker.hasPermission(PERMISSION))
    }

    @Test
    fun `hasAllPermissions returns true when all requested permissions are granted`() {
        every { ContextCompat.checkSelfPermission(context, PERMISSION) } returns PackageManager.PERMISSION_GRANTED
        every { ContextCompat.checkSelfPermission(context, SECOND_PERMISSION) } returns
            PackageManager.PERMISSION_GRANTED

        assertTrue(permissionChecker.hasAllPermissions(listOf(PERMISSION, SECOND_PERMISSION)))
    }

    @Test
    fun `hasAllPermissions returns false when at least one permission is denied`() {
        every { ContextCompat.checkSelfPermission(context, PERMISSION) } returns PackageManager.PERMISSION_GRANTED
        every { ContextCompat.checkSelfPermission(context, SECOND_PERMISSION) } returns PackageManager.PERMISSION_DENIED

        assertFalse(permissionChecker.hasAllPermissions(listOf(PERMISSION, SECOND_PERMISSION)))
    }

    private companion object {
        const val PERMISSION = "android.permission.RECORD_AUDIO"
        const val SECOND_PERMISSION = "android.permission.CAMERA"
    }
}
