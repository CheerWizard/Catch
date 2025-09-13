package com.cws.acatch.game

import com.cws.acatch.game.ui.GameScreen
import com.cws.kanvas.KanvasEntryPoint
import com.cws.kanvas.input.InputSensorManager

fun main() {
    KanvasEntryPoint(
        renderLoop = GameLoop(
            x = 0,
            y = 0,
            width = 800,
            height = 600,
            title = "Catch",
            inputSensorManager = InputSensorManager()
        )
    ) { renderLoop ->
        GameScreen(renderLoop as GameLoop)
    }
}