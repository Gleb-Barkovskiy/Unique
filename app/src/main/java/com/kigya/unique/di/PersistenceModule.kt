package com.kigya.unique.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.kigya.unique.data.local.db.LessonDatabase
import com.kigya.unique.data.remote.lesson.LessonApiSource
import com.kigya.unique.di.PersistenceModule.Constants.DATABASE_NAME
import com.kigya.unique.di.PersistenceModule.Constants.TIMETABLE_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): LessonDatabase =
        Room.databaseBuilder(app, LessonDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideLessonApi(lessonApi: LessonApiSource): LessonApiSource = lessonApi

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() },
            ),
            migrations = listOf(
                SharedPreferencesMigration(
                    appContext,
                    TIMETABLE_PREFERENCES,
                ),
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(TIMETABLE_PREFERENCES) },
        )
    }

    object Constants {
        const val TIMETABLE_PREFERENCES = "timetable_preferences"
        const val DATABASE_NAME = "lessons_database"
    }
}
