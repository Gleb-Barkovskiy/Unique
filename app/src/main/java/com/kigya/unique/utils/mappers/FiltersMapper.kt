package com.kigya.unique.utils.mappers

import com.kigya.unique.App
import com.kigya.unique.R

object FiltersMapper {
    fun getWeekOptionsStringValue(isAuto: Boolean): String =
        with(App.appContext.resources.getStringArray(R.array.week_options)) {
            return if (isAuto) this[0] else this[1]
        }

    fun getCourseHintByArrayIndex(index: Int): String =
        with(App.appContext.resources.getStringArray(R.array.course_options)) {
            return this[index - 1]
        }

    fun getGroupHintByArrayIndex(index: Int, course: Int): String {
        with(App.appContext.resources) {
            when (course) {
                1 -> with(getStringArray(R.array.group_options_1)) {
                    return this[index]
                }

                2 -> with(getStringArray(R.array.group_options_2)) {
                    return this[index]
                }

                3 -> with(getStringArray(R.array.group_options_3)) {
                    return this[index]
                }

                4 -> with(getStringArray(R.array.group_options_4)) {
                    return this[index]
                }

                else -> with(getStringArray(R.array.group_options_1)) {
                    return this[index]
                }
            }
        }
    }

    fun getResourceByCourse(course: Int) = when (course) {
        0 -> R.array.group_options_1
        1 -> R.array.group_options_2
        2 -> R.array.group_options_3
        3 -> R.array.group_options_4
        else -> R.array.group_options_1
    }

    fun getSubgroupBundle(list: List<String>) = list.joinToString(separator = " ")

    fun getSubgroupList(str: String) = str.split(" ")
}
