package com.nborba.vocalize.data

import android.content.Context
import com.nborba.vocalize.core.common.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

internal class GetRecordingFiles
    @Inject
    constructor(
        @ApplicationContext val context: Context,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        suspend operator fun invoke(): Result<List<File>> =
            withContext(ioDispatcher) {
                runCatching {
                    context.cacheDir
                        .listFiles { file -> file.isFile && file.extension == "m4a" }
                        ?.toList()
                        ?: emptyList()
                }
            }
    }
