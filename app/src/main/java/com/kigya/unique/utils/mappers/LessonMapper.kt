package com.kigya.unique.utils.mappers

object LessonMapper {
    fun getSubgroupRegularityPair(string: String): Pair<String, String> {
        return if (string.contains(" ") && string.contains("н")) {
            val split = string.split(" ")
            if (split[0].contains("н")) {
                Pair(split[1].take(1), split[0])
            } else {
                Pair(split[0].take(1), split[1])
            }
        } else {
            if (string.contains("н")) {
                Pair("", string)
            } else {
                Pair(string.take(1), "")
            }
        }
    }
}
