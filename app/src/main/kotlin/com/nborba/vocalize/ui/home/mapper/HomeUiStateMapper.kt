package com.nborba.vocalize.ui.home.mapper

import com.nborba.vocalize.R
import com.nborba.vocalize.core.common.util.StringProvider
import com.nborba.vocalize.ui.home.model.HomeUiState
import javax.inject.Inject

internal class HomeUiStateMapper
    @Inject
    constructor(
        private val stringProvider: StringProvider,
    ) {
        operator fun invoke(): HomeUiState =
            HomeUiState(
                title = stringProvider.getString(R.string.app_name),
                header = stringProvider.getString(R.string.home_header),
                buttonDetails = stringProvider.getString(R.string.home_button_details),
                buttonRecord = stringProvider.getString(R.string.home_button_record),
            )
    }
