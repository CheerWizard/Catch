package com.cws.kanvas

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock

expect class WindowID

interface BaseWindow {

    val eventListeners: MutableSet<EventListener>
    val events: ArrayDeque<Any>
    val lock: ReentrantLock

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

    fun pollEvents() {
        lock.withLock {
            while (events.isNotEmpty()) {
                dispatchEvent(events.removeFirst())
            }
        }
    }

    fun dispatchEvent(event: Any) {}

}

expect class Window : BaseWindow {

    companion object {
        fun free()
    }

    override val eventListeners: MutableSet<EventListener>
    override val events: ArrayDeque<Any>
    override val lock: ReentrantLock

    constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        title: String
    )

    fun release()
    fun isClosed(): Boolean
    fun applySwapChain()
    fun setSurface(surface: Any?)

}