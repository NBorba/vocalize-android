package com.nborba.vocalize.core.common.util

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(
        @StringRes resId: Int,
    ): String

    fun getString(
        @StringRes resId: Int,
        vararg formatArgs: Any,
    ): String
}
