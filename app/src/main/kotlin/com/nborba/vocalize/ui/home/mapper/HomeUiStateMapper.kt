package com.nborba.vocalize.ui.home.mapper

import com.nborba.vocalize.R
import com.nborba.vocalize.core.common.util.StringProvider
import com.nborba.vocalize.ui.home.model.HomeUiState
import javax.inject.Inject
import com.nborba.vocalize.core.designsystem.R as DSR

internal class HomeUiStateMapper
    @Inject
    constructor(
        private val stringProvider: StringProvider,
    ) {
        operator fun invoke(): HomeUiState {
            val buttonRecord = stringProvider.getString(DSR.string.button_record)
            return HomeUiState(
                title = stringProvider.getString(R.string.app_name),
                emptyTitle = stringProvider.getString(R.string.home_empty_title),
                emptyDescription =
                    stringProvider.getString(
                        R.string.home_empty_description,
                        buttonRecord,
                    ),
                buttonRecord = buttonRecord,
            )
        }
    }
