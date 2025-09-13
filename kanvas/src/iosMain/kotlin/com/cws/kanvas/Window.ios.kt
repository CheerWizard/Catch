package com.cws.kanvas

import kotlinx.atomicfu.locks.ReentrantLock

actual typealias WindowID = Unit

actual class Window : BaseWindow {

    actual companion object {
        actual fun free() = Unit
    }

    actual override val eventListeners: MutableSet<EventListener> = mutableSetOf()
    actual override val events: ArrayDeque<Any> = ArrayDeque()
    actual override val lock: ReentrantLock = ReentrantLock()

    actual constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        title: String
    ) {}

    actual fun release() {}

    actual fun isClosed(): Boolean = false

    actual fun applySwapChain() {}

    actual fun setSurface(surface: Any?) {}

    override fun dispatchEvent(event: Any) {
        super.dispatchEvent(event)
        // TODO: dispatch motion event for iOS
    }

}