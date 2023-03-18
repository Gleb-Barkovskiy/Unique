package com.kigya.unique.utils.system.intent

import android.app.Activity
import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kigya.unique.App
import com.kigya.unique.R
import com.kigya.unique.utils.system.intent.IntentCreator.IntentExtra.DIALOG_EXTRA
import com.kigya.unique.utils.system.intent.IntentCreator.IntentExtra.SPLASH_FIX

object IntentCreator {
    fun createShareImplicitIntent(activity: Activity, message: String, hint: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.apply {
            putExtra(Intent.EXTRA_TEXT, message)
            type = IntentType.TEXT_PLAIN_TYPE
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(Intent.createChooser(shareIntent, hint), null)
        }
    }

    fun createRestartIntent(activity: Activity, clazz: Class<out Activity>) {
        val options = ActivityOptions.makeBasic().toBundle()
        options.putInt(SPLASH_FIX, 1)
        val intent = Intent(activity.applicationContext, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.applicationContext.startActivity(intent, options)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_in)
    }

    fun createRestartIntentWithOpeningDialog(
        activity: Activity,
        clazz: Class<out Activity>,
        dialogName: String,
    ) {
        val options = ActivityOptions.makeBasic().toBundle()
        options.putInt(SPLASH_FIX, 1)
        val intent = Intent(activity.applicationContext, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(DIALOG_EXTRA, dialogName)
        activity.applicationContext.startActivity(intent, options)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_in)
    }

    fun getOpenWebsiteIntent(context: Context, url: String): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    fun createViewIntentByLink(uriString: String) {
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        App.appContext.startActivity(intent)
    }

    fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            App.appContext.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            openEmailInBrowser(email)
        }
    }

    private fun openEmailInBrowser(email: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val data = Uri.parse("mailto:$email")
        intent.data = data
        App.appContext.startActivity(intent)
    }

    object IntentType {
        const val TEXT_PLAIN_TYPE = "text/plain"
    }

    object IntentExtra {
        const val DIALOG_EXTRA = "dialog_extra"
        const val SPLASH_FIX = "android.activity.splashScreenStyle"
    }
}
