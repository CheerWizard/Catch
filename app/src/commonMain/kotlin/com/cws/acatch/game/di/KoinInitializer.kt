package com.cws.acatch.game.di

import com.cws.acatch.game.platform.platformModule
import org.koin.core.context.startKoin

object KoinInitializer {

    private var started = false

    fun init() {
        if (!started) {
            startKoin {
                modules(commonModule, platformModule)
            }
            started = true
        }
    }

}