package com.kigya.unique.adapters.lesson.interlayers

import android.app.Activity
import com.kigya.unique.R
import com.kigya.unique.databinding.TimetableItemBinding
import com.kigya.unique.utils.extensions.ui.view.setOnSafeClickListener
import com.kigya.unique.utils.lesson.LessonIntentHelper
import com.kigya.unique.utils.system.intent.IntentCreator

class LessonShareClickListener(private val activity: Activity) {
    operator fun invoke(viewBinding: TimetableItemBinding) {
        viewBinding.ibOptions.setOnSafeClickListener {
            IntentCreator.createShareImplicitIntent(
                activity = activity,
                message = LessonIntentHelper(viewBinding).createShareText(),
                hint = activity.getString(R.string.share_timetable_item),
            )
        }
    }
}
