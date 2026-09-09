package com.nborba.vocalize.ui.home.mapper

import com.nborba.vocalize.R
import com.nborba.vocalize.core.common.util.StringProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class HomeUiStateMapperTest {
    private val stringProvider: StringProvider =
        mockk {
            every { getString(R.string.app_name) } returns "Vocalize"
            every { getString(R.string.home_header) } returns "Welcome"
            every { getString(R.string.home_button_details) } returns "Details"
            every { getString(R.string.home_button_record) } returns "Record"
        }
    private val mapper = HomeUiStateMapper(stringProvider)

    @Test
    fun `invoke maps localized strings to HomeUiState`() {
        val state = mapper()

        assertEquals("Vocalize", state.title)
        assertEquals("Welcome", state.header)
        assertEquals("Details", state.buttonDetails)
        assertEquals("Record", state.buttonRecord)
    }
}
