package com.kigya.unique.utils.extensions.context

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

private const val MILLIS_SHOT = 10L

fun Context.onTouchResponseVibrate(block: () -> Unit) {
    val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        getSystemService(Vibrator::class.java) as Vibrator
    }

    vib.vibrate(VibrationEffect.createOneShot(MILLIS_SHOT, VibrationEffect.DEFAULT_AMPLITUDE))
    block()
}
