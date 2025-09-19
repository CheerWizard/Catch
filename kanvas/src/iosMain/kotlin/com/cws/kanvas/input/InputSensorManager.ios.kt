package com.cws.kanvas.input

import com.cws.klog.KLog
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue

@OptIn(ExperimentalForeignApi::class)
actual class InputSensorManager {

    actual val sensor: InputSensor = InputSensor()

    private val motionManager = CMMotionManager()

    actual fun init() {
        if (!motionManager.isAccelerometerAvailable()) {
            KLog.error("Accelerometer is not available on Native platform")
            return
        }

        motionManager.accelerometerUpdateInterval = 0.01

        motionManager.startAccelerometerUpdatesToQueue(NSOperationQueue.mainQueue) { data, error ->
            data?.let {
                it.acceleration.useContents {
                    sensor.acceleration.x = x.toFloat()
                    sensor.acceleration.y = y.toFloat()
                    sensor.acceleration.z = z.toFloat()
                    sensor.acceleration.normalized()
                }
            }
        }
    }

    actual fun release() {
        motionManager.stopAccelerometerUpdates()
    }

}