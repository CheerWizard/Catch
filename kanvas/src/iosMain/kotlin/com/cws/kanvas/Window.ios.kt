package com.cws.kanvas

actual typealias WindowID = Unit

actual class Window {

    actual companion object {
        actual fun free() = Unit
    }

    actual constructor(
        width: Int,
        height: Int,
        title: String,
        surface: Any?,
    ) {}

    actual fun release() {}

    actual fun isClosed(): Boolean = false

    actual fun applySwapChain() {}

    actual inline fun pollEvents(crossinline block: () -> Unit) {}

    actual fun setCurrent() {}

}