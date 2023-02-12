package com.kigya.unique

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import kotlin.properties.Delegates

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var appContext: Context by Delegates.notNull()
    }
}
