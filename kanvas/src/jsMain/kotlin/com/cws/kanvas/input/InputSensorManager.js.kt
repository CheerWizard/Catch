package com.cws.kanvas.input

actual class InputSensorManager {

    actual val sensor: InputSensor = InputSensor()

    actual fun init() = Unit
    actual fun release() = Unit

}