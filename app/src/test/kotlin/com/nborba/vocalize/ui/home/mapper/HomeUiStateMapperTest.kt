package com.nborba.vocalize.ui.home.mapper

import com.nborba.vocalize.HomeUiStateFixture
import com.nborba.vocalize.R
import com.nborba.vocalize.core.common.util.StringProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.nborba.vocalize.core.designsystem.R as DSR

internal class HomeUiStateMapperTest {
    private val stringProvider: StringProvider =
        mockk {
            every { getString(R.string.app_name) } returns defaultUiState.title
            every { getString(R.string.home_empty_title) } returns defaultUiState.emptyTitle
            every { getString(DSR.string.button_record) } returns defaultUiState.buttonRecord
            every {
                getString(
                    R.string.home_empty_description,
                    defaultUiState.buttonRecord,
                )
            } returns defaultUiState.emptyDescription
        }
    private val mapper = HomeUiStateMapper(stringProvider)

    @Test
    fun `invoke maps localized strings to HomeUiState with null effect`() {
        val state = mapper()
        assertEquals(defaultUiState, state)
    }

    private companion object {
        val defaultUiState = HomeUiStateFixture.default()
    }
}
