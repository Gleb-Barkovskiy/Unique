package com.kigya.unique.adapters.lesson

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller

class LessonSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return LOCAL_METRICS / displayMetrics.densityDpi
    }

    companion object {
        private const val LOCAL_METRICS = 130f
    }
}