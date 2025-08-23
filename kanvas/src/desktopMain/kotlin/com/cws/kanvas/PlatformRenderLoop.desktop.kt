package com.cws.kanvas

import kotlin.concurrent.thread

actual open class PlatformRenderLoop actual constructor(name: String, priority: Int) {

    protected actual var running: Boolean = false

    private val thread: Thread = thread(
        start = false,
        name = name,
        priority = priority,
        block = ::runLoop
    )

    private var prevTimeNanos = 0L

    actual fun startLoop() {
        if (running) return
        running = true
        thread.start()
    }

    actual fun stopLoop() {
        if (!running) return
        running = false
        thread.join()
    }

    private fun runLoop() {
        onCreate()
        var dtMillis = 16.0f
        prevTimeNanos = System.nanoTime()
        while (running) {
            onUpdate(dtMillis)
            val timeNanos = System.nanoTime()
            dtMillis = (timeNanos - prevTimeNanos) / 1_000_000.0f
            prevTimeNanos = timeNanos
        }
        onDestroy()
    }

    protected actual open fun onCreate() = Unit
    protected actual open fun onDestroy() = Unit
    protected actual open fun onUpdate(dtMillis: Float) = Unit

}