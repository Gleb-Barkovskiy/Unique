package com.kigya.unique.di

import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.data.local.settings.AppSettingsSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    abstract fun bindAppSettings(
        appSettings: AppSettings
    ): AppSettingsSource
}