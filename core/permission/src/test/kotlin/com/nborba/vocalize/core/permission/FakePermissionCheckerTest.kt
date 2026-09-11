package com.nborba.vocalize.core.permission

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class FakePermissionCheckerTest {
    @Test
    fun `when defaultGranted is true and no permissions set, returns true`() {
        val checker = FakePermissionChecker(defaultGranted = true)

        assertTrue(checker.hasPermission(PERMISSION))
        assertTrue(checker.hasAllPermissions(listOf(PERMISSION, SECOND_PERMISSION)))
    }

    @Test
    fun `when defaultGranted is false and no permissions set, returns false`() {
        val checker = FakePermissionChecker(defaultGranted = false)

        assertFalse(checker.hasPermission(PERMISSION))
        assertFalse(checker.hasAllPermissions(listOf(PERMISSION, SECOND_PERMISSION)))
    }

    @Test
    fun `when permission explicitly granted or revoked, respects setPermissionGranted`() {
        val checker = FakePermissionChecker(defaultGranted = false)

        checker.setPermissionGranted(PERMISSION, isGranted = true)
        assertTrue(checker.hasPermission(PERMISSION))
        assertFalse(checker.hasPermission(SECOND_PERMISSION))

        checker.setPermissionGranted(PERMISSION, isGranted = false)
        assertFalse(checker.hasPermission(PERMISSION))
    }

    private companion object {
        const val PERMISSION = "android.permission.RECORD_AUDIO"
        const val SECOND_PERMISSION = "android.permission.CAMERA"
    }
}
