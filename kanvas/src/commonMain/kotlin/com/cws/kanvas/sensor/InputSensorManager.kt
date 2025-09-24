package com.cws.kanvas.sensor

expect class InputSensorManager {

    val sensor: InputSensor

    fun init()
    fun release()

}