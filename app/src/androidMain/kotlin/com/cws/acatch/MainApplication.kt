package com.cws.acatch

import android.app.Application
import com.cws.acatch.di.platformModule
import com.cws.acatch.di.commonModule
import com.cws.kanvas.di.startKanvasKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKanvasKoin(applicationContext) {
            modules(commonModule, platformModule)
        }
    }

}