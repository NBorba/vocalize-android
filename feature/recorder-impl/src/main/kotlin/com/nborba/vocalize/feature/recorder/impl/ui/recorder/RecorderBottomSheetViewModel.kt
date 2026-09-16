package com.nborba.vocalize.feature.recorder.impl.ui.recorder

import android.Manifest
import androidx.lifecycle.ViewModel
import com.nborba.vocalize.core.permission.domain.PermissionChecker
import com.nborba.vocalize.core.permission.host.PermissionResult
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderEffect
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderState
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class RecorderBottomSheetViewModel
    @Inject
    constructor(
        private val permissionChecker: PermissionChecker,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(RecorderUiState())
        val uiState: StateFlow<RecorderUiState> = _uiState.asStateFlow()

        val effects = uiState.map { it.effect }

        init {
            onMainButtonClick()
        }

        fun onMainButtonClick() {
            when (getRecorderState()) {
                RecorderState.Idle -> startRecording()
                RecorderState.Recording -> pauseRecording()
                RecorderState.Paused -> resumeRecording()
            }
        }

        fun onDismissRequest() {
            if (getRecorderState() == RecorderState.Idle) {
                _uiState.update { it.copy(effect = RecorderEffect.Dismiss()) }
            } else {
                stopRecording()
                _uiState.update { it.copy(effect = RecorderEffect.Dismiss("Recording saved")) }
            }
        }

        fun onAudioPermissionRequestResult(result: PermissionResult) {
            if (result == PermissionResult.Granted) {
                onMainButtonClick()
            } else {
                _uiState.update { it.copy(effect = RecorderEffect.Dismiss()) }
            }
        }

        fun onEffectConsumed() {
            _uiState.update { it.copy(effect = null) }
        }

        private fun getRecorderState(): RecorderState = uiState.value.state

        private fun startRecording() {
            requireAudioPermission {
                _uiState.update { it.copy(state = RecorderState.Recording) }
            }
        }

        private fun pauseRecording() {
            _uiState.update { it.copy(state = RecorderState.Paused) }
        }

        private fun resumeRecording() {
            requireAudioPermission {
                _uiState.update { it.copy(state = RecorderState.Recording) }
            }
        }

        private fun stopRecording() {
            _uiState.update { it.copy(state = RecorderState.Idle) }
        }

        private fun requireAudioPermission(onPermissionGranted: () -> Unit) {
            if (permissionChecker.hasPermission(AUDIO_PERMISSION)) {
                onPermissionGranted()
            } else {
                requestAudioPermission()
            }
        }

        private fun requestAudioPermission() {
            _uiState.update { it.copy(effect = RecorderEffect.RequestPermission(AUDIO_PERMISSION)) }
        }

        private companion object {
            const val AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
        }
    }
