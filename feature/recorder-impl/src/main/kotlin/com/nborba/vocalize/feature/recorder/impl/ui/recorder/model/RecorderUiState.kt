package com.nborba.vocalize.feature.recorder.impl.ui.recorder.model

sealed interface RecorderEffect {
    data class RequestPermission(
        val permission: String,
    ) : RecorderEffect

    data class ShowToast(
        val message: String,
    ) : RecorderEffect
}

data class RecorderUiState(
    val title: String,
    val buttonRecord: String,
    val effect: RecorderEffect? = null,
)
