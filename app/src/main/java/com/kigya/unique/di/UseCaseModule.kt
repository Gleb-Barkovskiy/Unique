package com.kigya.unique.di

import androidx.work.WorkManager
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.usecase.LessonsUseCase
import com.kigya.unique.usecase.ScheduleWorkUseCase
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
    ): LessonsUseCase = LessonsUseCase(appSettings, repository)

    @Provides
    fun provideScheduleWorkUseCase(
        workManager: WorkManager,
    ): ScheduleWorkUseCase = ScheduleWorkUseCase(workManager)
}
