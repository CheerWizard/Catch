package com.cws.kanvas.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.cws.kanvas.math.Vec3
import kotlin.math.abs

actual class InputSensorManager(context: Context) : SensorEventListener {

    actual val sensor: InputSensor = InputSensor()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    actual fun init() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    actual fun release() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        val x = event.values[0] * 10f
        val y = event.values[1] * 10f
        val z = event.values[2] * 10f

        sensor.acceleration = Vec3(abs(x), abs(y), abs(z))
        sensor.direction.x = if (x > 3f) -1f else 1f
        sensor.direction.y = if (y > 3f) 1f else -1f
        sensor.direction.z = if (z > 3f) -1f else 1f
    }

}