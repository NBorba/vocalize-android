package com.nborba.vocalize.core.common.util

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class DefaultStringProvider
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : StringProvider {
        override fun getString(
            @StringRes resId: Int,
        ): String = context.getString(resId)

        override fun getString(
            @StringRes resId: Int,
            vararg formatArgs: Any,
        ): String = context.getString(resId, *formatArgs)
    }
