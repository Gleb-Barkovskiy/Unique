package com.kigya.unique.di

import androidx.work.WorkManager
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.usecase.StudentUseCase
import com.kigya.unique.usecase.ScheduleWorkUseCase
import com.kigya.unique.usecase.SetupUseCase
import com.kigya.unique.usecase.TeacherUseCase
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
    fun provideStudentUseCase(
        appSettings: AppSettings,
        repository: LessonRepository,
    ): StudentUseCase = StudentUseCase(appSettings, repository)

    @Provides
    fun provideTeacherUseCase(
        appSettings: AppSettings,
        repository: LessonRepository,
    ): TeacherUseCase = TeacherUseCase(appSettings, repository)

    @Provides
    fun provideScheduleWorkUseCase(
        workManager: WorkManager,
    ): ScheduleWorkUseCase = ScheduleWorkUseCase(workManager)
}
