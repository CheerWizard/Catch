package com.cws.acatch.game.platform

expect class GameSensorManager {

    var sensor: GameSensorData

    fun init()
    fun release()

}