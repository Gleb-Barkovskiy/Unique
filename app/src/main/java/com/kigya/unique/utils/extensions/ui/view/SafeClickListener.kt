package com.kigya.unique.utils.extensions.ui.view

import android.os.SystemClock
import android.view.View

class SafeListener(
    private val interval: Int = 1000,
    private val onSafeClick: (View) -> Unit,
) : View.OnClickListener {

    private var lastClickTime: Long = 0L

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}

fun View.setOnSafeClickListener(
    onSafeClick: (View) -> Unit,
) {
    setOnClickListener(
        SafeListener { v ->
            onSafeClick(v)
        },
    )
}

fun View.setOnSafeClickListener(
    interval: Int,
    onSafeClick: (View) -> Unit,
) {
    setOnClickListener(
        SafeListener(interval) { v ->
            onSafeClick(v)
        },
    )
}
