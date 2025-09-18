package com.cws.acatch.game

import androidx.compose.ui.window.ComposeUIViewController
import com.cws.acatch.game.di.KoinInitializer
import com.cws.acatch.game.ui.GameScreen
import org.koin.compose.koinInject

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer.init()
    }
) {
    GameScreen(koinInject())
}