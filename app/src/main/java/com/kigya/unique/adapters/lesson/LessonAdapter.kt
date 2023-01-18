package com.kigya.unique.adapters.lesson

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kigya.unique.App
import com.kigya.unique.R
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.databinding.TimetableItemBinding
import com.kigya.unique.utils.LessonList
import com.kigya.unique.utils.extensions.specific.lesson.properRegularity
import com.kigya.unique.utils.extensions.specific.lesson.properSubgroup
import com.kigya.unique.utils.extensions.specific.lesson.timeEnd
import com.kigya.unique.utils.extensions.specific.lesson.timeStart
import com.kigya.unique.utils.extensions.ui.view.delayOnLifecycle
import com.kigya.unique.utils.extensions.ui.view.setOnSafeClickListener

class LessonAdapter(
    private val activity: Activity,
) : RecyclerView.Adapter<LessonAdapter.LessonsViewHolder>() {

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
        with(binding) {
            setViewSafeClickListener()
            setOnShareButtonSafeClickListener()
        }
        return LessonsViewHolder(binding)
    }

    private fun TimetableItemBinding.setOnShareButtonSafeClickListener() {
        ibOptions.setOnSafeClickListener { shareTimetableItemIntent() }
    }

    private fun TimetableItemBinding.setViewSafeClickListener() {
        mlContainer.apply {
            resetMotionAnimation()
            setOnSafeClickListener {
                transitionToEnd {
                    delayOnLifecycle(durationOnMillis = PROGRESS_VIEW_STATIC_DELAY) {
                        transitionToStart()
                    }
                }
            }
        }
    }

    private fun TimetableItemBinding.shareTimetableItemIntent() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.apply {
            putExtra(Intent.EXTRA_TEXT, createShareText())
            type = INTENT_TYPE
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(
                Intent.createChooser(
                    shareIntent,
                    App.appContext.getString(SHARE_TEXT_RES)
                ), null
            )
        }
    }

    private fun TimetableItemBinding.createShareText(): String {
        return """
            ${this.tvLessonName.text}
            ${this.tvTeacher.text}
            ${this.tvLessonStart.text} - ${this.tvLessonEnd.text} 
            ${this.tvAudience.text}
        """.trimIndent()
    }

    private fun MotionLayout.resetMotionAnimation() {
        if (progress != 0f) progress = 0f
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        val listItem = lessons[position]
        with(holder.binding) { bindLesson(listItem) }
    }

    private fun TimetableItemBinding.bindLesson(listItem: Lesson) {
        with(listItem) {
            tvLessonStart.text = time.timeStart()
            tvLessonEnd.text = time.timeEnd()
            tvLessonName.text = subject
            tvTeacher.text = teacher
            tvAudience.text = audience
            tvRegularity.text = regularity.properRegularity()
            tvType.text = type
            tvSubgroup.text = subgroup.properSubgroup()
        }
    }

    fun updateList(newList: LessonList) {
        val diffResult = DiffUtil.calculateDiff(LessonDiffUtilCallback(lessons, newList))
        lessons = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class LessonsViewHolder(
        val binding: TimetableItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val PROGRESS_VIEW_STATIC_DELAY = 1000L
        private const val INTENT_TYPE = "text/plain"
        private val SHARE_TEXT_RES = R.string.share_timetable_item
    }
}

