package com.kigya.unique.adapters.lesson

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.databinding.TimetableItemBinding
import com.kigya.unique.utils.LessonList
import com.kigya.unique.utils.lesson.properRegularity
import com.kigya.unique.utils.lesson.properSubgroup
import com.kigya.unique.utils.lesson.timeEnd
import com.kigya.unique.utils.lesson.timeStart


class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonsViewHolder>() {

    private var lessons = emptyList<Lesson>()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = lessons.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TimetableItemBinding.inflate(inflater, parent, false)
        return LessonsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        val listItem = lessons[position]
        with(holder.binding) { bindLesson(listItem) }
    }

    private fun TimetableItemBinding.bindLesson(listItem: Lesson) {
        tvLessonStart.text = listItem.time.timeStart()
        tvLessonEnd.text = listItem.time.timeEnd()
        tvLessonName.text = listItem.subject
        tvTeacher.text = listItem.teacher
        tvAudience.text = listItem.audience
        tvRegularity.text = listItem.regularity.properRegularity()
        tvType.text = listItem.type
        tvSubgroup.text = listItem.subgroup.properSubgroup()
    }

    fun updateList(newList: LessonList) {
        val diffResult = DiffUtil.calculateDiff(LessonDiffUtilCallback(lessons, newList))
        lessons = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class LessonsViewHolder(
        val binding: TimetableItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

