package com.cws.kanvas

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock

expect class WindowID

abstract class BaseWindow {

    protected val eventListeners = mutableSetOf<EventListener>()
    protected val events = ArrayDeque<Any>()

    private val lock = ReentrantLock()

    fun addEventListener(eventListener: EventListener) {
        lock.withLock {
            eventListeners.add(eventListener)
        }
    }

    fun removeEventListener(eventListener: EventListener) {
        lock.withLock {
            eventListeners.remove(eventListener)
        }
    }

    fun addEvent(event: Any) {
        lock.withLock {
            events.addLast(event)
        }
    }

    open fun pollEvents() {
        while (events.isNotEmpty()) {
            dispatchEvent(events.removeFirst())
        }
    }

    open fun dispatchEvent(event: Any) {}

}

expect class Window : BaseWindow {

    companion object {
        fun free()
    }

    constructor(
        width: Int,
        height: Int,
        title: String,
        surface: Any? = null
    )

    fun release()
    fun isClosed(): Boolean
    fun applySwapChain()
    fun setCurrent()

}