package com.nborba.vocalize.core.permission.di

import com.nborba.vocalize.core.permission.data.AndroidPermissionChecker
import com.nborba.vocalize.core.permission.domain.PermissionChecker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency injection module providing the singleton [PermissionChecker].
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class PermissionModule {
    /**
     * Binds [AndroidPermissionChecker] to [PermissionChecker].
     */
    @Binds
    @Singleton
    abstract fun bindPermissionChecker(impl: AndroidPermissionChecker): PermissionChecker
}
