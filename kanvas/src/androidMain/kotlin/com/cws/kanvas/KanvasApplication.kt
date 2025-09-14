package com.cws.kanvas

import android.app.Application

open class KanvasApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AssetProvider.init(applicationContext)
    }

}