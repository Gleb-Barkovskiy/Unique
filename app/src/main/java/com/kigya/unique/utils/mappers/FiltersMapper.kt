package com.kigya.unique.utils.mappers

import com.kigya.unique.App
import com.kigya.unique.R

object FiltersMapper {
    fun Boolean.toWeekHint(): String =
        with(App.appContext.resources.getStringArray(R.array.week_options)) {
            return if (this@toWeekHint) this[0] else this[1]
        }

    fun Int.toCourseHint(): String =
        with(App.appContext.resources.getStringArray(R.array.course_options)) {
            return this[this@toCourseHint - 1]
        }

    fun Int.toGroupHint(course: Int): String {
        with(App.appContext.resources) {
            return when (course) {
                1 -> with(getStringArray(R.array.group_options_1)) {
                    return this[this@toGroupHint - 1]
                }

                2 -> with(getStringArray(R.array.group_options_2)) {
                    return this[this@toGroupHint - 1]
                }

                3 -> with(getStringArray(R.array.group_options_3)) {
                    return this[this@toGroupHint - 1]
                }

                4 -> with(getStringArray(R.array.group_options_4)) {
                    return this[this@toGroupHint - 1]
                }

                else -> "Курс"
            }
        }

    }

    fun List<String>.toSubgroupBundle() = this.joinToString(separator = " ")

    fun String.toSubgroupList() = this.split(" ")

}