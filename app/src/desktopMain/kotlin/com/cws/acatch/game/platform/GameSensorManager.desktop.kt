package com.cws.acatch.game.platform

actual class GameSensorManager {

    actual var sensor: GameSensorData = GameSensorData.create()

    actual fun init() = Unit
    actual fun release() = Unit

}