package com.nborba.vocalize.feature.recorder.impl.ui.recorder.mapper

import com.nborba.vocalize.core.common.util.StringProvider
import com.nborba.vocalize.feature.recorder.impl.R
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RecorderUiStateMapperTest {
    private val stringProvider: StringProvider =
        mockk {
            every { getString(R.string.recorder_title) } returns "Recorder"
        }
    private val mapper = RecorderUiStateMapper(stringProvider)

    @Test
    fun `invoke maps localized strings to RecorderUiState`() {
        val state = mapper()

        assertEquals("Recorder", state.title)
    }
}
