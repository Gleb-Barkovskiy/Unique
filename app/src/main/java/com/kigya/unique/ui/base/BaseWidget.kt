package com.kigya.unique.ui.base

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.kigya.unique.R
import com.kigya.unique.utils.system.intent.IntentCreator

open class BaseWidget(
    private val layoutId: Int,
) : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId, layoutId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        layoutId: Int,
    ) {
        val views = RemoteViews(context.packageName, layoutId)
        views.setOnClickPendingIntent(
            R.id.btnWebsite,
            IntentCreator.getOpenWebsiteIntent(context, WEBSITE_LINK),
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        const val WEBSITE_LINK = "https://mmf.bsu.by/ru/raspisanie-zanyatij/"
    }
}
