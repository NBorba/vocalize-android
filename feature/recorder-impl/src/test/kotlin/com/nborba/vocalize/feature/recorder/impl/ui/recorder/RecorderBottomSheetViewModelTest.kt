package com.nborba.vocalize.feature.recorder.impl.ui.recorder

import android.Manifest
import com.nborba.vocalize.core.common.util.MainDispatcherExtension
import com.nborba.vocalize.core.common.util.StringProvider
import com.nborba.vocalize.core.permission.domain.PermissionChecker
import com.nborba.vocalize.core.permission.host.PermissionResult
import com.nborba.vocalize.feature.recorder.impl.R
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderEffect
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

internal class RecorderBottomSheetViewModelTest {
    @JvmField
    @RegisterExtension
    val mainCoroutinesDispatcher = MainDispatcherExtension()

    private val permissionChecker: PermissionChecker = mockk()
    private val stringProvider: StringProvider = mockk()
    private lateinit var viewModel: RecorderBottomSheetViewModel

    @BeforeEach
    fun setUp() {
        every { permissionChecker.hasPermission(Manifest.permission.RECORD_AUDIO) } returns true
        every { stringProvider.getString(R.string.recorder_recording_saved) } returns "Recording has been saved"
    }

    @Test
    fun `when audio permission granted, init starts recording`() {
        viewModel = viewModel(permissionChecker = permissionChecker)

        assertEquals(RecorderState.Recording, viewModel.uiState.value.state)
    }

    @Test
    fun `when audio permission not granted, init requests permission`() {
        every { permissionChecker.hasPermission(Manifest.permission.RECORD_AUDIO) } returns false

        viewModel = viewModel(permissionChecker = permissionChecker)

        assertEquals(RecorderState.Idle, viewModel.uiState.value.state)
        assertEquals(
            RecorderEffect.RequestPermission(Manifest.permission.RECORD_AUDIO),
            viewModel.uiState.value.effect,
        )
    }

    @Test
    fun `when onMainButtonClick while recording, pauses recording`() {
        viewModel = viewModel(permissionChecker = permissionChecker)

        assertEquals(RecorderState.Recording, viewModel.uiState.value.state)

        viewModel.onMainButtonClick()

        assertEquals(RecorderState.Paused, viewModel.uiState.value.state)
    }

    @Test
    fun `when onMainButtonClick while paused, resumes recording`() {
        viewModel = viewModel(permissionChecker = permissionChecker)
        viewModel.onMainButtonClick() // Recording -> Paused
        assertEquals(RecorderState.Paused, viewModel.uiState.value.state)

        viewModel.onMainButtonClick() // Paused -> Recording

        assertEquals(RecorderState.Recording, viewModel.uiState.value.state)
    }

    @Test
    fun `when onDismissRequest while idle, emits Dismiss effect`() {
        every { permissionChecker.hasPermission(Manifest.permission.RECORD_AUDIO) } returns false

        viewModel = viewModel(permissionChecker = permissionChecker)
        assertEquals(RecorderState.Idle, viewModel.uiState.value.state)

        viewModel.onDismissRequest()

        assertEquals(RecorderState.Idle, viewModel.uiState.value.state)
        assertEquals(RecorderEffect.Dismiss(), viewModel.uiState.value.effect)
    }

    @Test
    fun `when onDismissRequest while recording, emits Dismiss effect and stops recording`() {
        viewModel = viewModel(permissionChecker = permissionChecker)

        assertEquals(RecorderState.Recording, viewModel.uiState.value.state)

        viewModel.onDismissRequest()

        assertEquals(RecorderState.Idle, viewModel.uiState.value.state)
        assertEquals(
            RecorderEffect.Dismiss("Recording has been saved"),
            viewModel.uiState.value.effect,
        )
    }

    @Test
    fun `when onDismissRequest while paused, emits Dismiss effect and stops recording`() {
        viewModel = viewModel(permissionChecker = permissionChecker)

        viewModel.onMainButtonClick() // Recording -> Paused
        assertEquals(RecorderState.Paused, viewModel.uiState.value.state)

        viewModel.onDismissRequest() // Paused -> Idle

        assertEquals(RecorderState.Idle, viewModel.uiState.value.state)
        assertEquals(
            RecorderEffect.Dismiss("Recording has been saved"),
            viewModel.uiState.value.effect,
        )
    }

    @Test
    fun `when onAudioPermissionRequestResult Granted, starts recording`() {
        every { permissionChecker.hasPermission(Manifest.permission.RECORD_AUDIO) } returns false
        viewModel = viewModel(permissionChecker = permissionChecker)

        every { permissionChecker.hasPermission(Manifest.permission.RECORD_AUDIO) } returns true
        viewModel.onAudioPermissionRequestResult(PermissionResult.Granted)

        assertEquals(RecorderState.Recording, viewModel.uiState.value.state)
    }

    @Test
    fun `when onAudioPermissionRequestResult Denied, emits Dismiss effect`() {
        every { permissionChecker.hasPermission(Manifest.permission.RECORD_AUDIO) } returns false
        viewModel = viewModel(permissionChecker = permissionChecker)

        viewModel.onAudioPermissionRequestResult(PermissionResult.Denied)

        assertEquals(RecorderEffect.Dismiss(), viewModel.uiState.value.effect)
    }

    @Test
    fun `when onEffectConsumed, clears effect`() {
        every { permissionChecker.hasPermission(Manifest.permission.RECORD_AUDIO) } returns false
        viewModel = viewModel(permissionChecker = permissionChecker)

        viewModel.onEffectConsumed()

        assertNull(viewModel.uiState.value.effect)
    }

    private fun viewModel(permissionChecker: PermissionChecker): RecorderBottomSheetViewModel =
        RecorderBottomSheetViewModel(
            permissionChecker = permissionChecker,
            stringProvider = stringProvider,
        )
}
