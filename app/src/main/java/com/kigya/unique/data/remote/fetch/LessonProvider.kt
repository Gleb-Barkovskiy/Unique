package com.kigya.unique.data.remote.fetch

import com.kigya.unique.utils.LessonList
import retrofit2.http.GET

interface LessonProvider {
    //TODO("Separate methods")
    @GET(".")
    suspend fun getAllLessons():LessonList

}