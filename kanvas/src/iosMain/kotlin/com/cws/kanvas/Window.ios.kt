package com.cws.kanvas

actual typealias WindowID = Unit

actual class Window : BaseWindow {

    actual companion object {
        actual fun free() = Unit
    }

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

    actual fun setCurrent() {}

    actual fun setSurface(surface: Any?) {}

    override fun dispatchEvent(event: Any) {
        super.dispatchEvent(event)
        // TODO: dispatch motion event for iOS
    }

}