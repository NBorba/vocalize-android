package com.nborba.vocalize.ui.detail.mapper

import com.nborba.vocalize.R
import com.nborba.vocalize.core.common.util.StringProvider
import com.nborba.vocalize.ui.detail.model.DetailUiState
import javax.inject.Inject
import com.nborba.vocalize.core.designsystem.R as DSR

internal class DetailUiStateMapper
    @Inject
    constructor(
        private val stringProvider: StringProvider,
    ) {
        operator fun invoke(id: String): DetailUiState =
            DetailUiState(
                title = stringProvider.getString(R.string.detail_title, id),
                header = stringProvider.getString(R.string.detail_header),
                buttonBack = stringProvider.getString(DSR.string.button_back),
            )
    }
