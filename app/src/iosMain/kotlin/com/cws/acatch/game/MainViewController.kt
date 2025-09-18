package com.cws.acatch.game

import androidx.compose.ui.window.ComposeUIViewController
import com.cws.acatch.game.di.KoinInitializer
import com.cws.acatch.game.ui.GameScreen

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer.init()
    }
) {
    GameScreen()
}