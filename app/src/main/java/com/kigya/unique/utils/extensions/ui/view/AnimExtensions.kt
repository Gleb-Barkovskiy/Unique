package com.kigya.unique.utils.extensions.ui.view

import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot

private const val DEFAULT_DURATION = 500L
private const val LOTTIE_MAX_FRAME = 99

fun View.playFadeOutAnimation(
    duration: Long = DEFAULT_DURATION,
    visibility: Int = View.INVISIBLE,
    completion: (() -> Unit)? = null,
) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }
}

fun View.playFadeInAnimation(
    duration: Long = DEFAULT_DURATION,
    completion: (() -> Unit)? = null,
) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}

fun ImageButton.setDrawableAnimated(
    duration: Long = DEFAULT_DURATION,
    drawable: Int,
    completion: (() -> Unit)? = null,
) {
    animate()
        .alpha(0f)
        .setDuration(duration / 2)
        .withEndAction {
            this.setImageResource(drawable)
            this.animate()
                .alpha(1f)
                .setDuration(duration / 2)
                .withEndAction {
                    completion?.let {
                        it()
                    }
                }
        }
}

fun LottieAnimationView.preventDisappearing() = this.setMaxFrame(LOTTIE_MAX_FRAME)

fun View.startSidesCircularReveal(fromLeft: Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int,
        ) {
            v.removeOnLayoutChangeListener(this)
            val cx = if (fromLeft) v.left else v.right
            val cy = v.bottom
            val radius = hypot(right.toDouble(), bottom.toDouble()).toInt()
            ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, radius.toFloat()).apply {
                interpolator = DecelerateInterpolator(2f)
                duration = 1000
                start()
            }
        }
    })
}

fun View.startCenterCircularReveal() {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int,
        ) {
            v.removeOnLayoutChangeListener(this)
            val cx = v.width / 2
            val cy = v.height / 2
            val radius = hypot(right.toDouble(), bottom.toDouble()).toInt()
            ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, radius.toFloat()).apply {
                interpolator = DecelerateInterpolator(2f)
                duration = 2000
                start()
            }
        }
    })
}

fun View.findLocationOfCenterOnTheScreen(): IntArray {
    val positions = intArrayOf(0, 0)
    getLocationInWindow(positions)
    positions[0] = positions[0] + width / 2
    positions[1] = positions[1] + height / 2
    return positions
}

fun View.subsidenceEffect() {
    animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction {
        animate().scaleX(1f).scaleY(1f).duration = 100
    }
}

fun View.scaleUpEffect() {
    animate().scaleX(1.3f).scaleY(1.3f).duration = 2000
}

fun View.moveToCenter() {
    val x = (parent as View).width / 2 - width / 2
    animate().x(x.toFloat()).duration = 1000
}

fun View.delayOnLifecycle(
    durationOnMillis: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit,
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(durationOnMillis)
        block()
    }
}
