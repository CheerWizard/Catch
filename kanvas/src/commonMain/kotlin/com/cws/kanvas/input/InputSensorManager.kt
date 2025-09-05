package com.cws.kanvas.input

expect class InputSensorManager {

    val sensor: InputSensor

    fun init()
    fun release()

}