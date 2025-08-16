package com.cws.acatch.core

import android.app.Application
import com.cws.acatch.BuildConfig
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}