package com.kigya.unique.utils.extensions.ui.view

import android.view.View
import com.kigya.unique.ui.survey.onboarding.listeners.OnSwipeTouchListener

fun View.setOnLeftSwipeTouchListener(action: (View) -> Unit) {
    setOnTouchListener(object : OnSwipeTouchListener(context) {
        override fun onSwipeLeft() {
            action.invoke(this@setOnLeftSwipeTouchListener)
        }
    })
}