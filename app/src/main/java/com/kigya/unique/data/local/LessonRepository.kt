package com.kigya.unique.data.local

import androidx.room.withTransaction
import com.google.gson.Gson
import com.kigya.unique.App
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.data.local.db.LessonDatabase
import com.kigya.unique.data.remote.lesson.LessonApi
import com.kigya.unique.data.remote.networkBoundResource
import com.kigya.unique.utils.LessonListResource
import com.kigya.unique.utils.wrappers.Resource
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import javax.inject.Inject

class LessonRepository @Inject constructor(
    private val lessonsApi: LessonApi,
    private val database: LessonDatabase,
) {

    private val lessonDao = database.getLessonDao()

    fun getDatabaseSize() = lessonDao.getDatabaseSize()

    fun getLessons(): Flow<LessonListResource> = networkBoundResource(
        query = {
            lessonDao.getAllLessons()
        },
        fetch = {
            lessonsApi.getNetworkData()
        },
        saveFetchResult = { rows ->
            database.withTransaction {
                if (rows.isNotEmpty()) lessonDao.deleteAllLessons()
                lessonDao.upsertLessons(rows)
            }
        },
    )

    suspend fun getNetworkData() {
        lessonsApi.getNetworkData()
    }

    fun getDatabaseLessons(
        course: Int,
        group: Int,
        day: String?,
        subgroupList: List<String>,
        regularity: String?,
    ): Flow<Resource<List<Lesson>>> = networkBoundResource(
        query = {
            lessonDao.getLessons(course, group, day, subgroupList, regularity)
        },
        fetch = {},
        saveFetchResult = {},
    )

    fun getDatabaseLessons(
        teacher: String,
        day: String?,
        regularity: String?,
    ): Flow<Resource<List<Lesson>>> = networkBoundResource(
        query = {
            lessonDao.getLessons(teacher, day, regularity)
        },
        fetch = {},
        saveFetchResult = {},
    )

    fun getAllLessons() = networkBoundResource(
        query = {
            lessonDao.getAllLessons()
        },
        fetch = {},
        saveFetchResult = {},
    )

    suspend fun setToDatabaseFromAssets() {
        val inputStream: InputStream = App.appContext.assets.open(FILE_NAME)
        val inputString = inputStream.bufferedReader().use { it.readText() }
        val gson = Gson()
        val lessons = gson.fromJson(inputString, Array<Lesson>::class.java).toList()
        lessonDao.upsertLessons(lessons)
    }

    companion object {
        private const val FILE_NAME = "lessons.json"
    }
}
