package com.cws.kanvas

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement

actual typealias WindowID = Any

actual class Window : BaseWindow {

    actual companion object {
        actual fun free() = Unit
    }

    actual override val eventListeners: MutableSet<EventListener> = mutableSetOf()
    actual override val events: ArrayDeque<Any> = ArrayDeque()
    actual override val lock: ReentrantLock = ReentrantLock()

    private var handle: WindowID

    actual constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        title: String
    ) {
        val canvas = document.getElementById("canvas") as HTMLCanvasElement?
            ?: error("Canvas element with id canvas is not found")
        canvas.width = width
        canvas.height = height
        canvas.title = title
        handle = canvas
        initEventListeners()
    }

    actual fun release() {
        window.close()
    }

    actual fun isClosed(): Boolean = window.closed

    actual fun applySwapChain() = Unit

    actual fun setCurrent() = Unit

    actual fun setSurface(surface: Any?) = Unit

    private fun initEventListeners() {
        window.onresize = { e ->
            eventListeners.forEach { it.onWindowResized(window.innerWidth, window.innerHeight) }
        }

        window.onkeydown = { e ->
            eventListeners.forEach { it.onKeyPressed(e.code.toKeyCode(), e.repeat) }
        }

        window.onkeyup = { e ->
            eventListeners.forEach { it.onKeyReleased(e.code.toKeyCode()) }
        }

        window.onkeypress = { e ->
            eventListeners.forEach { it.onKeyTyped(e.key[0]) }
        }

        window.onmousedown = { e ->
            eventListeners.forEach { it.onMousePressed(e.buttons.toMouseCode(), false) }
        }

        window.onmouseup = { e ->
            eventListeners.forEach { it.onMouseReleased(e.buttons.toMouseCode()) }
        }

        window.onmousemove = { e ->
            eventListeners.forEach { it.onMouseMove(e.x, e.y) }
        }

        window.onscroll = { e ->
            eventListeners.forEach { it.onMouseScroll(window.scrollX, window.scrollY) }
        }
    }

    private fun String.toKeyCode(): KeyCode {
        return when (this) {
            "Space" -> KeyCode.Space
            "World1" -> KeyCode.World1
            "World2" -> KeyCode.World2
            "Escape" -> KeyCode.Esc
            "Enter" -> KeyCode.Enter
            "Tab" -> KeyCode.Tab
            "Backspace" -> KeyCode.Backspace
            "Insert" -> KeyCode.Insert
            "Delete" -> KeyCode.Delete
            "ArrowRight" -> KeyCode.Right
            "ArrowLeft" -> KeyCode.Left
            "ArrowDown" -> KeyCode.Down
            "ArrowUp" -> KeyCode.Up
            "PageUp" -> KeyCode.PageUp
            "PageDown" -> KeyCode.PageDown
            "Home" -> KeyCode.Home
            "End" -> KeyCode.End
            "CapsLock" -> KeyCode.CapsLock
            "ScrollLock" -> KeyCode.ScrollLock
            "NumLock" -> KeyCode.NumLock
            "PrintScreen" -> KeyCode.PrintScreen
            "Pause" -> KeyCode.Pause
            "F1" -> KeyCode.F1
            "F2" -> KeyCode.F2
            "F3" -> KeyCode.F3
            "F4" -> KeyCode.F4
            "F5" -> KeyCode.F5
            "F6" -> KeyCode.F6
            "F7" -> KeyCode.F7
            "F8" -> KeyCode.F8
            "F9" -> KeyCode.F9
            "F10" -> KeyCode.F10
            "F11" -> KeyCode.F11
            "F12" -> KeyCode.F12
            "Numpad0" -> KeyCode.KP0
            "Numpad1" -> KeyCode.KP1
            "Numpad2" -> KeyCode.KP2
            "Numpad3" -> KeyCode.KP3
            "Numpad4" -> KeyCode.KP4
            "Numpad5" -> KeyCode.KP5
            "Numpad6" -> KeyCode.KP6
            "Numpad7" -> KeyCode.KP7
            "Numpad8" -> KeyCode.KP8
            "Numpad9" -> KeyCode.KP9
            "NumpadDecimal" -> KeyCode.KPDecimal
            "NumpadDivide" -> KeyCode.KPDivide
            "NumpadMultiply" -> KeyCode.KPMultiply
            "NumpadSubtract" -> KeyCode.KPSubtract
            "NumpadAdd" -> KeyCode.KPAdd
            "NumpadEnter" -> KeyCode.KPEnter
            "NumpadEqual" -> KeyCode.KPEqual
            "ShiftLeft" -> KeyCode.LeftShift
            "ControlLeft" -> KeyCode.LeftControl
            "AltLeft" -> KeyCode.LeftAlt
            "MetaLeft" -> KeyCode.LeftSuper
            "ShiftRight" -> KeyCode.RightShift
            "ControlRight" -> KeyCode.RightControl
            "AltRight" -> KeyCode.RightAlt
            "MetaRight" -> KeyCode.RightSuper
            "ContextMenu" -> KeyCode.Menu
            else -> KeyCode.Null
        }
    }

    private fun Short.toMouseCode(): MouseCode {
        return when (this) {
            1.toShort() -> MouseCode.Left
            2.toShort() -> MouseCode.Right
            4.toShort() -> MouseCode.Middle
            else -> MouseCode.Null
        }
    }

}