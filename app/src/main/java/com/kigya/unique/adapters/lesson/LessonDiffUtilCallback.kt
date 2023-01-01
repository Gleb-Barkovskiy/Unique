package com.kigya.unique.adapters.lesson

import androidx.recyclerview.widget.DiffUtil
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.ui.tabs.LessonList

class LessonDiffUtilCallback(
    private val oldList: LessonList,
    private val newList: LessonList
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}