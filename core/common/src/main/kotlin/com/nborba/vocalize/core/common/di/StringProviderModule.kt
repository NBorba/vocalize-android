package com.nborba.vocalize.core.common.di

import android.content.Context
import com.nborba.vocalize.core.common.util.DefaultStringProvider
import com.nborba.vocalize.core.common.util.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object StringProviderModule {
    @Singleton
    @Provides
    fun provideStringProvider(
        @ApplicationContext context: Context,
    ): StringProvider = DefaultStringProvider(context)
}
