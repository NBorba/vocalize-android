package com.nborba.vocalize.feature.recorder.impl.ui.recorder

import androidx.lifecycle.ViewModel
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.mapper.RecorderUiStateMapper
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class RecorderBottomSheetViewModel
    @Inject
    constructor(
        recorderUiStateMapper: RecorderUiStateMapper,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(recorderUiStateMapper())
        val uiState: StateFlow<RecorderUiState> = _uiState.asStateFlow()
    }
