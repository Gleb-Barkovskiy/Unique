package com.kigya.unique.utils.lesson

import com.kigya.unique.databinding.TimetableItemBinding

class LessonIntentHelper(private val viewBinding: TimetableItemBinding) {
    fun createShareText(): String = with(viewBinding) {
        return@createShareText """
        ${tvLessonName.text}
        ${tvTeacher.text}
        ${tvLessonStart.text} - ${tvLessonEnd.text} 
        ${tvAudience.text}
        """.trimIndent()
    }
}
