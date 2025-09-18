package com.cws.acatch.game

import com.cws.acatch.game.di.commonModule
import com.cws.acatch.game.platform.platformModule
import com.cws.acatch.game.ui.GameScreen
import com.cws.kanvas.KanvasApp

fun main() {
    KanvasApp<GameLoop>(
        startKoin = {
            modules(commonModule, platformModule)
        }
    ) {
        GameScreen()
    }
}