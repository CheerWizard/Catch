package com.cws.kanvas.core

import android.os.Looper
import android.view.Choreographer
import java.util.concurrent.CountDownLatch

actual open class PlatformRenderLoop actual constructor(name: String, priority: Int)
    : Thread(), Choreographer.FrameCallback {

    protected actual var running = false

    private lateinit var choreographer: Choreographer
    private val ready = CountDownLatch(1)
    private var previousTimeNanos = 0L

    init {
        this.name = name
        this.priority = priority
    }

    override fun run() {
        Looper.prepare()
        choreographer = Choreographer.getInstance()
        ready.countDown()
        onCreate()
        Looper.loop()
        onDestroy()
    }

    actual fun startLoop() {
        if (running) return
        running = true
        start()
        ready.await()
        choreographer.postFrameCallback(this)
    }

    actual fun stopLoop() {
        if (!running) return
        running = false
        choreographer.removeFrameCallback(this)
        Looper.myLooper()?.quitSafely()
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (!running) return
        val dtNanos = frameTimeNanos - previousTimeNanos
        previousTimeNanos = frameTimeNanos
        onUpdate(dtNanos / 1_000_000.0f)
        choreographer.postFrameCallback(this)
    }

    protected actual open fun onCreate() = Unit
    protected actual open fun onDestroy() = Unit
    protected actual open fun onUpdate(dtMillis: Float) = Unit

}