package com.nborba.vocalize.feature.recorder.impl.ui.recorder.mapper

import com.nborba.vocalize.core.common.util.StringProvider
import com.nborba.vocalize.feature.recorder.impl.R
import com.nborba.vocalize.feature.recorder.impl.ui.recorder.model.RecorderUiState
import javax.inject.Inject

internal class RecorderUiStateMapper
    @Inject
    constructor(
        private val stringProvider: StringProvider,
    ) {
        operator fun invoke(): RecorderUiState =
            RecorderUiState(
                title = stringProvider.getString(R.string.recorder_title),
                buttonRecord = "Record",
            )
    }
