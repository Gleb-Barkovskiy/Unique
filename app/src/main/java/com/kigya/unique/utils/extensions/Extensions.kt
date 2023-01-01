package com.kigya.unique.utils.extensions

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.*
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.AbsListView
import android.widget.ImageButton
import android.widget.ScrollView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.kigya.unique.R
import com.kigya.unique.utils.result.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.ui.survey.onboarding.OnSwipeTouchListener
import com.kigya.unique.utils.result.*
import kotlin.math.hypot
import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.ui.views.ResourceView
import com.kigya.unique.utils.Resource

fun <T> MutableLiveData<T>.share(): LiveData<T> = this

fun <T> Fragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                onCollect(it)
            }
        }
    }
}

fun <T> ViewModel.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewModelScope.launch {
        flow.collect {
            onCollect(it)
        }
    }
}


fun String.fastReplace(from: String, to: String): String {
    val sb = StringBuilder()
    var i = 0
    while (i < this.length) {
        when {
            this[i] == from[0] -> {
                var j = 0
                while (j < from.length && i + j < this.length) {
                    if (this[i + j] != from[j]) break
                    j++
                }
                if (j == from.length) {
                    sb.append(to)
                    i += j
                } else {
                    sb.append(this[i])
                    i++
                }
            }
            else -> {
                sb.append(this[i])
                i++
            }
        }
    }
    return sb.toString()
}

fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun <T> LiveData<T>.requireValue(): T {
    return this.value ?: throw IllegalStateException("Value is empty")
}

typealias ViewModelCreator<VM> = () -> VM

class ViewModelFactory<VM : ViewModel>(
    private val viewModelCreator: ViewModelCreator<VM>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelCreator() as T
    }
}

inline fun <reified VM : ViewModel> Fragment.viewModelCreator(noinline creator: ViewModelCreator<VM>): Lazy<VM> {
    return viewModels { ViewModelFactory(creator) }
}

fun View.fadeOutAnimation(
    duration: Long = 500,
    visibility: Int = View.INVISIBLE,
    completion: (() -> Unit)? = null
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

fun View.fadeInAnimation(duration: Long = 500, completion: (() -> Unit)? = null) {
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
    duration: Long = 500,
    drawable: Int,
    completion: (() -> Unit)? = null
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

fun LottieAnimationView.preventDissapearing() = this.setMaxFrame(99)

fun View.setOnLeftSwipeTouchListener(action: (View) -> Unit) {
    setOnTouchListener(object : OnSwipeTouchListener(context) {
        override fun onSwipeLeft() {
            action.invoke(this@setOnLeftSwipeTouchListener)
        }
    })
}

fun Context.onTouchResponseVibrate(block: () -> Unit) {
    val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    vib.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
    block()
}

fun View.startSidesCircularReveal(fromLeft: Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int,
            oldRight: Int, oldBottom: Int
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
            v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int,
            oldRight: Int, oldBottom: Int
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

fun String?.mapToAccountType() = when (this) {
    AccountType.STUDENT.name -> AccountType.STUDENT
    AccountType.TEACHER.name -> AccountType.TEACHER
    else -> AccountType.STUDENT
}

fun AccountType?.mapToString() = when (this) {
    AccountType.STUDENT -> AccountType.STUDENT.name
    AccountType.TEACHER -> AccountType.TEACHER.name
    else -> AccountType.STUDENT.name
}

fun <T> BaseFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                onCollect(it)
            }
        }
    }
}

fun <T, R> BaseFragment.observeResource(
    flow: Flow<T>,
    resourceView: ResourceView,
    onSuccess: (R) -> Unit
) = collectFlow(flow) { result ->
    resourceView.setResource(this, result as Resource<*>)
    if (result is Resource.Success<*>) {
        @Suppress("UNCHECKED_CAST")
        onSuccess(result.data as R)
    }
}


