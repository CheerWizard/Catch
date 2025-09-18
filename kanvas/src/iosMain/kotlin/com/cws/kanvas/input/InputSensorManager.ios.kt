package com.cws.kanvas.input

import com.cws.klog.KLog
import kotlinx.cinterop.ExperimentalForeignApi
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
                sensor.acceleration.x = it.acceleration.getValue()
                sensor.acceleration.normalized()
            }
        }
    }

    actual fun release() {
        motionManager.stopAccelerometerUpdates()
    }

}