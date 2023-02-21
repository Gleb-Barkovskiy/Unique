package com.kigya.unique.adapters.lesson

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.adapters.lesson.interlayers.LessonExpandClickListener
import com.kigya.unique.adapters.lesson.interlayers.LessonShareClickListener
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.databinding.TimetableItemBinding
import com.kigya.unique.utils.extensions.specific.lesson.getEndTime
import com.kigya.unique.utils.extensions.specific.lesson.getRegularityFullStringValue
import com.kigya.unique.utils.extensions.specific.lesson.getStartTime
import com.kigya.unique.utils.extensions.specific.lesson.getSubgroupFullStringValue

class LessonAdapter(
    private val activity: Activity,
    private val isStudent: Boolean,
) : ListAdapter<Lesson, LessonAdapter.LessonsViewHolder>(LessonDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder =
        LessonsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.timetable_item, parent, false),
        )

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class LessonsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding by viewBinding(TimetableItemBinding::bind)

        fun bind(lesson: Lesson) {
            LessonExpandClickListener()(viewBinding)
            LessonShareClickListener(activity)(viewBinding)
            bindLesson(lesson)
        }

        private fun bindLesson(listItem: Lesson) {
            with(listItem) {
                with(viewBinding) {
                    if (isStudent) {
                        gCourseGroup.isVisible = false
                    } else {
                        gCourseGroup.isVisible = true
                        tvCourse.text = activity.applicationContext.getString(
                            R.string.course_template,
                            course.toString(),
                        )
                        tvGroup.text = activity.applicationContext.getString(
                            R.string.group_template,
                            group.toString(),
                        )
                    }
                    tvLessonStart.text = getStartTime(time)
                    tvLessonEnd.text = getEndTime(time)
                    tvLessonName.text = subject
                    tvTeacher.text = teacher
                    tvAudience.text = audience
                    tvRegularity.text = getRegularityFullStringValue(regularity)
                    tvType.text = type
                    tvSubgroup.text = getSubgroupFullStringValue(subgroup)
                }
            }
        }
    }
}
