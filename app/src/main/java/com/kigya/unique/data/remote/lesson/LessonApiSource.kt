package com.kigya.unique.data.remote.lesson

import com.kigya.unique.utils.LessonList

interface LessonApiSource {
    suspend fun getNetworkData(): LessonList
}
