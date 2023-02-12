package com.kigya.unique.utils.system.intent

import android.app.Activity
import android.content.Intent
import com.kigya.unique.R

object IntentCreator {
    fun creareShareImplicitIntent(activity: Activity, message: String, hint: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.apply {
            putExtra(Intent.EXTRA_TEXT, message)
            type = IntentType.TEXT_PLAIN_TYPE
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(Intent.createChooser(shareIntent, hint), null)
        }
    }

    fun createRestartIntent(activity: Activity, clazz: Class<out Activity>) {
        val intent = Intent(activity.applicationContext, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.applicationContext.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_in)
    }

    object IntentType {
        const val TEXT_PLAIN_TYPE = "text/plain"
    }
}
