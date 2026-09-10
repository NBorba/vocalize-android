package com.nborba.vocalize.ui.detail.mapper

import com.nborba.vocalize.R
import com.nborba.vocalize.core.common.util.StringProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import com.nborba.vocalize.core.designsystem.R as DSR

internal class DetailUiStateMapperTest {
    private val stringProvider: StringProvider =
        mockk {
            every { getString(R.string.detail_title, TEST_ID) } returns "Detail #1"
            every { getString(R.string.detail_header) } returns "Viewing Detail"
            every { getString(DSR.string.button_back) } returns "Go back"
        }
    private val mapper = DetailUiStateMapper(stringProvider)

    @Test
    fun `invoke maps localized strings to HomeUiState and null effect`() {
        val state = mapper(id = TEST_ID)

        assertEquals("Detail #1", state.title)
        assertEquals("Viewing Detail", state.header)
        assertEquals("Go back", state.buttonBack)
        assertNull(state.effect)
    }

    private companion object {
        const val TEST_ID: String = "123"
    }
}
