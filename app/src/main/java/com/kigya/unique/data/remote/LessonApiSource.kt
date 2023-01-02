package com.kigya.unique.data.remote

import com.kigya.unique.utils.LessonList

interface LessonApiSource {
    suspend fun getNetworkData(): LessonList
}