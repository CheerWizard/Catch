package com.cws.kanvas

expect class WindowID

open class BaseWindow {

    protected val eventListeners = mutableSetOf<EventListener>()
    protected val events = ArrayDeque<Any>()

    fun addEventListener(eventListener: EventListener) {
        eventListeners.add(eventListener)
    }

    fun removeEventListener(eventListener: EventListener) {
        eventListeners.remove(eventListener)
    }

    fun addEvent(event: Any) {
        events.addLast(event)
    }

    protected fun popEvents() {
        while (events.isNotEmpty()) {

        }
    }

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
    fun pollEvents()

}