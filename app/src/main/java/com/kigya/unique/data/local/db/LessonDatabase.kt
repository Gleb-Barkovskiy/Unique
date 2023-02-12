package com.kigya.unique.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kigya.unique.data.dto.lesson.Lesson

@Database(
    entities = [Lesson::class],
    version = 1,
    exportSchema = false,
)
abstract class LessonDatabase : RoomDatabase() {
    abstract fun getLessonDao(): LessonDao
}
