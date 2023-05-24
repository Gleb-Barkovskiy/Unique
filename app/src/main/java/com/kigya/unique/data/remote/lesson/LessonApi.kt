package com.kigya.unique.data.remote.lesson

import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.data.remote.fetch.LessonProvider
import com.kigya.unique.utils.LessonList
import com.kigya.unique.utils.constants.ModelConst
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class LessonApi @Inject constructor(
    private val provider: LessonProvider,
) : LessonApiSource {

    override suspend fun getNetworkData(): LessonList {
        val rowList = mutableListOf<Lesson>()
        coroutineScope {
            (1..4).map { course ->
                val maxGroup = when (course) {
                    1 -> ModelConst.Groups.MAX_FIRST_COURSE
                    2 -> ModelConst.Groups.MAX_SECOND_COURSE
                    3 -> ModelConst.Groups.MAX_THIRD_COURSE
                    4 -> ModelConst.Groups.MAX_FOURTH_COURSE
                    else -> 0
                }
                (1..maxGroup).map { group ->
                    async {
                        rowList += getNetworkDataByCourseAndGroup(course, group, false)
                    }
                }
            }
            (1..4).map { course ->
                async {
                    rowList += getNetworkDataByCourseAndGroup(course, 0, true)
                }
            }
        }
        return rowList
    }

    private suspend fun getNetworkDataByCourseAndGroup(
        course: Int,
        group: Int,
        isMilitaryFaculty: Boolean,
    ): LessonList {
        return provider.getAllLessons().toMutableList().filter { lesson ->
            lesson.course == course && lesson.group == group }
    }
}
