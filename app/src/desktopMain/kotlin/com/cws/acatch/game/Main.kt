package com.cws.acatch.game

import com.cws.kanvas.input.InputSensorManager
import com.cws.acatch.game.ui.GameScreen
import com.cws.kanvas.KanvasApp

fun main() {
    KanvasApp(
        renderLoop = GameLoop(
            x = 400,
            y = 300,
            width = 800,
            height = 600,
            title = "Catch",
            inputSensorManager = InputSensorManager()
        )
    ) { renderLoop ->
        GameScreen(renderLoop)
    }
}