@file:Suppress("SameReturnValue")

package com.kigya.unique.utils.extensions.specific.lesson

import com.kigya.unique.utils.constants.ModelConst.FIRST_WEEK_STRING_SHORT
import com.kigya.unique.utils.extensions.specific.lesson.Const.DELIMITER_BETWEEN_TIMETABLE_TIME
import com.kigya.unique.utils.extensions.specific.lesson.Const.EVERY_WEEK_FULL
import com.kigya.unique.utils.extensions.specific.lesson.Const.FIRST_WEEK_FULL
import com.kigya.unique.utils.extensions.specific.lesson.Const.SECOND_WEEK_FULL
import com.kigya.unique.utils.extensions.string.empty

fun getStartTime(str: String) =
    str.split(DELIMITER_BETWEEN_TIMETABLE_TIME)[0].trim()

fun getEndTime(str: String) =
    str.split(DELIMITER_BETWEEN_TIMETABLE_TIME)[1].trim()

fun getSubgroupFullStringValue(str: String?) =
    if (str.isNullOrBlank()) {
        String.empty()
    } else {
        "${Const.SUBGROUP} $str"
    }

fun getRegularityFullStringValue(str: String?) =
    if (str.isNullOrBlank()) {
        EVERY_WEEK_FULL
    } else {
        if (str == FIRST_WEEK_STRING_SHORT) {
            FIRST_WEEK_FULL
        } else {
            SECOND_WEEK_FULL
        }
    }

private object Const {
    const val EVERY_WEEK_FULL = "Каждую неделю"
    const val FIRST_WEEK_FULL = "Первая неделя"
    const val SECOND_WEEK_FULL = "Вторая неделя"

    const val SUBGROUP = "Подгруппа"

    const val DELIMITER_BETWEEN_TIMETABLE_TIME = "–"
}
