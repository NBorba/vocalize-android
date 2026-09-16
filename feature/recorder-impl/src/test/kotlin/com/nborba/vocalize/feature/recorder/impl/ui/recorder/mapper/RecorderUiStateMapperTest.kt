package com.nborba.vocalize.feature.recorder.impl.ui.recorder.mapper

import com.nborba.vocalize.RecorderUiStateFixture
import com.nborba.vocalize.core.common.util.StringProvider
import com.nborba.vocalize.feature.recorder.impl.R
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.nborba.vocalize.core.designsystem.R as DSR

internal class RecorderUiStateMapperTest {
    private val stringProvider: StringProvider =
        mockk {
            every { getString(R.string.recorder_title) } returns defaultUiState.title
            every { getString(DSR.string.button_record) } returns defaultUiState.buttonRecord
        }
    private val mapper = RecorderUiStateMapper(stringProvider)

    @Test
    fun `invoke maps localized strings to RecorderUiState`() {
        val state = mapper()
        assertEquals(defaultUiState, state)
    }

    private companion object {
        val defaultUiState = RecorderUiStateFixture.default()
    }
}
