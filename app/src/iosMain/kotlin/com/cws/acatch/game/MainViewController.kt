package com.cws.acatch.game

import com.cws.acatch.game.di.commonModule
import com.cws.acatch.game.di.platformModule
import com.cws.acatch.game.ui.GameScreen
import com.cws.kanvas.core.KanvasViewController
import com.cws.kanvas.core.RenderLoop
import com.cws.kanvas.di.startKanvasKoin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewController : KoinComponent {

    val controller: KanvasViewController

    init {
        startKanvasKoin {
            modules(commonModule, platformModule)
        }
        controller = KanvasViewController(inject<GameLoop>() as RenderLoop) {
            GameScreen()
        }
    }

}