package com.kigya.unique.di

import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.usecase.LoadLessonsUseCase
import com.kigya.unique.usecase.SetupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideSetupUseCase(
        appSettings: AppSettings,
    ): SetupUseCase = SetupUseCase(appSettings)

    @Provides
    fun provideLoadLessonsUseCase(
        appSettings: AppSettings,
        repository: LessonRepository,
    ): LoadLessonsUseCase = LoadLessonsUseCase(appSettings, repository)
}
