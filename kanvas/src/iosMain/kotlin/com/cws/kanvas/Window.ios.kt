package com.cws.kanvas

actual typealias WindowID = Unit

actual class Window : BaseWindow {

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

    actual fun setCurrent() {}

    override fun dispatchEvent(event: Any) {
        super.dispatchEvent(event)
        // TODO: dispatch motion event for iOS
    }

}