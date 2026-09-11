package com.nborba.vocalize.core.permission.util

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

internal class ContextExtensionsTest {
    private val context: Context = mockk()

    @BeforeEach
    fun setUp() {
        mockkStatic(ContextCompat::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ContextCompat::class)
    }

    @Test
    fun `hasPermission returns true when ContextCompat check returns PERMISSION_GRANTED`() {
        every { ContextCompat.checkSelfPermission(context, PERMISSION) } returns PackageManager.PERMISSION_GRANTED

        assertTrue(context.hasPermission(PERMISSION))
    }

    @Test
    fun `hasPermission returns false when ContextCompat check returns PERMISSION_DENIED`() {
        every { ContextCompat.checkSelfPermission(context, PERMISSION) } returns PackageManager.PERMISSION_DENIED

        assertFalse(context.hasPermission(PERMISSION))
    }

    private companion object {
        const val PERMISSION = "android.permission.RECORD_AUDIO"
    }
}
