package com.kigya.unique.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.kigya.unique.utils.logger.LogCatLogger
import com.kigya.unique.utils.logger.Logger

@Module
@InstallIn(SingletonComponent::class)
class StuffsModule {
    @Provides
    fun provideLogger(): Logger {
        return LogCatLogger
    }
}