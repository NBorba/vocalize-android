package com.nborba.vocalize.feature.recorder.impl.ui.recorder.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.nborba.vocalize.core.designsystem.icon.VocalizeIcons

sealed interface RecorderEffect {
    data class RequestPermission(
        val permission: String,
    ) : RecorderEffect

    data class ShowToast(
        val message: String,
    ) : RecorderEffect

    data class Dismiss(
        val message: String? = null,
    ) : RecorderEffect
}

enum class RecorderState {
    Idle,
    Recording,
    Paused,
}

data class RecorderUiState(
    val state: RecorderState = RecorderState.Idle,
    val effect: RecorderEffect? = null,
) {
    val mainButtonIcon: ImageVector
        get() =
            when (state) {
                RecorderState.Idle -> VocalizeIcons.Record
                RecorderState.Recording -> VocalizeIcons.Pause
                RecorderState.Paused -> VocalizeIcons.Record
            }
}
