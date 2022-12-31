package com.kigya.unique.utils.lesson

fun String.Companion.empty(): String {
    return ""
}

fun String.timeStart(): String {
    return this.split("–")[0].trim()
}

fun String.timeEnd(): String {
    return this.split("–")[1].trim()
}

fun String?.properSubgroup(): String {
    return if (this.isNullOrBlank()) String.empty() else {
        "${Const.SUBGROUP} $this"
    }
}

fun String?.properRegularity(): String {
    return if (this.isNullOrBlank()) {
        Const.EVERY_WEEK
    } else {
        if (this == "1н") {
            Const.FIRST_WEEK
        } else {
            Const.SECOND_WEEK
        }
    }
}

private object Const {
    const val EVERY_WEEK = "Каждую неделю"
    const val FIRST_WEEK = "Первая неделя"
    const val SECOND_WEEK = "Вторая неделя"

    const val SUBGROUP = "Подгруппа"
}