package com.kigya.unique.utils.extensions.ui.view

import android.view.View
import com.kigya.unique.ui.listeners.OnSwipeTouchListener

fun View.setOnLeftSwipeTouchListener(action: (View) -> Unit) {
    setOnTouchListener(object : OnSwipeTouchListener(context) {
        override fun onSwipeLeft() {
            action.invoke(this@setOnLeftSwipeTouchListener)
        }
    })
}

fun View.setOnSidesSwipeTouchListener(
    leftAction: (View) -> Unit = {},
    rightAction: (View) -> Unit = {},
) {
    setOnTouchListener(object : OnSwipeTouchListener(context) {
        override fun onSwipeLeft() {
            leftAction(this@setOnSidesSwipeTouchListener)
        }

        override fun onSwipeRight() {
            rightAction(this@setOnSidesSwipeTouchListener)
        }
    })
}
