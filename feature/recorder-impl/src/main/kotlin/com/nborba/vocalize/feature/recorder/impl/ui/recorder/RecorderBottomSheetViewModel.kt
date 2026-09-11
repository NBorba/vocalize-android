package com.nborba.vocalize.feature.recorder.impl.ui.recorder

import android.Manifest
import androidx.lifecycle.ViewModel
import com.nborba.vocalize.core.permission.domain.PermissionChecker
import com.nborba.vocalize.core.permission.host.PermissionResult
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.mapper.RecorderUiStateMapper
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderEffect
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
        recorderUiStateMapper: RecorderUiStateMapper,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(recorderUiStateMapper())
        val uiState: StateFlow<RecorderUiState> = _uiState.asStateFlow()

        val effects = uiState.map { it.effect }

        fun onRecordButtonClick() {
            if (isAudioPermissionEnabled()) {
                startRecording()
            } else {
                requestAudioPermission()
            }
        }

        fun requestAudioPermission() {
            _uiState.update { it.copy(effect = RecorderEffect.RequestPermission(AUDIO_PERMISSION)) }
        }

        fun onAudioPermissionRequestResult(result: PermissionResult) {
            if (result == PermissionResult.Granted) {
                startRecording()
            } else {
                _uiState.update { it.copy(effect = RecorderEffect.ShowToast("Audio permission denied.")) }
            }
        }

        fun onEffectConsumed() {
            _uiState.update { it.copy(effect = null) }
        }

        private fun isAudioPermissionEnabled(): Boolean = permissionChecker.hasPermission(AUDIO_PERMISSION)

        private fun startRecording() {
            _uiState.update { it.copy(effect = RecorderEffect.ShowToast("Recording started...")) }
        }

        private companion object {
            const val AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
        }
    }
