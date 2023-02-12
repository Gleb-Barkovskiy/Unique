package com.kigya.unique.di

import com.kigya.unique.utils.logger.LogCatLogger
import com.kigya.unique.utils.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StuffsModule {
    @Provides
    fun provideLogger(): Logger {
        return LogCatLogger
    }
}
