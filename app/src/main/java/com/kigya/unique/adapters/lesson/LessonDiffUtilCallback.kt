package com.kigya.unique.adapters.lesson

import androidx.recyclerview.widget.DiffUtil
import com.kigya.unique.data.dto.lesson.Lesson

object LessonDiffUtilCallback : DiffUtil.ItemCallback<Lesson>() {
    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean =
        oldItem == newItem
}
