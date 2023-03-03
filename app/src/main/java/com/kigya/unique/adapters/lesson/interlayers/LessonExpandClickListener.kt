package com.kigya.unique.adapters.lesson.interlayers

import androidx.constraintlayout.motion.widget.MotionLayout
import com.kigya.unique.databinding.TimetableItemBinding
import com.kigya.unique.utils.extensions.ui.view.delayOnLifecycle
import com.kigya.unique.utils.extensions.ui.view.setOnSafeClickListener

class LessonExpandClickListener {
    operator fun invoke(viewBinding: TimetableItemBinding) {
        viewBinding.mlContainer.apply {
            resetMotionAnimation(this)
            setOnSafeClickListener {
                transitionToEnd {
                    delayOnLifecycle(durationOnMillis = PROGRESS_VIEW_STATIC_DELAY) {
                        transitionToStart()
                    }
                }
            }
        }
    }

    private fun resetMotionAnimation(motionLayout: MotionLayout) {
        if (motionLayout.progress != 0f) motionLayout.progress = 0f
    }

    companion object {
        private const val PROGRESS_VIEW_STATIC_DELAY = 1000L
    }
}
