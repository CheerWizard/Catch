package com.cws.acatch.game

import com.cws.acatch.di.commonModule
import com.cws.acatch.di.platformModule
import com.cws.acatch.game.ui.GameScreen
import com.cws.kanvas.core.KanvasApp
import com.cws.kanvas.di.startKanvasKoin

fun main() {
    startKanvasKoin {
        modules(commonModule, platformModule)
    }
    KanvasApp<GameLoop> {
        GameScreen()
    }
}