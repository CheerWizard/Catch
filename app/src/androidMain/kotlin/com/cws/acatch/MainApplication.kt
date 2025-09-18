package com.cws.acatch

import com.cws.acatch.game.di.commonModule
import com.cws.acatch.game.platform.platformModule
import com.cws.kanvas.KanvasApplication
import com.cws.klog.KLog
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : KanvasApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            androidLogger(if (KLog.enabled) Level.DEBUG else Level.NONE)
            modules(commonModule, platformModule)
        }
    }

}