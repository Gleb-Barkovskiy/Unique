package com.kigya.unique.data.local

import androidx.room.withTransaction
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.data.local.db.LessonDatabase
import com.kigya.unique.data.remote.LessonApi
import com.kigya.unique.data.remote.Resource
import com.kigya.unique.data.remote.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LessonRepository @Inject constructor(
    private val lessonsApi: LessonApi,
    private val database: LessonDatabase
) {

    private val lessonDao = database.getLessonDao()

    fun getLessons(
        course: Int,
        group: Int,
        day: String?,
        subgroup: String?,
        regularity: String?
    ): Flow<Resource<List<Lesson>>> = networkBoundResource(
        query = {
            lessonDao.getLessons(course, group, day, subgroup, regularity)
        },
        fetch = {
            lessonsApi.getNetworkData()
        },
        saveFetchResult = { rows ->
            database.withTransaction {
                if (rows.isNotEmpty()) lessonDao.deleteAllLessons()
                lessonDao.upsertLessons(rows)
            }
        }
    )

    fun getDatabaseLessons(
        course: Int,
        group: Int,
        day: String?,
        subgroup: String?,
        regularity: String?
    ): Flow<Resource<List<Lesson>>> = networkBoundResource(
        query = {
            lessonDao.getLessons(course, group, day, subgroup, regularity)
        },
        fetch = {},
        saveFetchResult = {}
    )

}