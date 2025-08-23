package com.cws.kanvas

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL

actual typealias WindowID = Long

actual class Window : BaseWindow {

    actual companion object {
        private var libraryInitialized: Boolean = false

        actual fun free() {
            glfwTerminate()
        }
    }

    private var handle: WindowID = Kanvas.NULL.toLong()

    actual constructor(
        width: Int,
        height: Int,
        title: String,
        surface: Any?,
    ) {
        if (!libraryInitialized) {
            if (!glfwInit()) {
                throw RuntimeException("Failed to initialize GLFW")
            }
            GL.createCapabilities()
            libraryInitialized = true
        }
        handle = glfwCreateWindow(width, height, title, 0, 0)
        setCurrent()
        initEventListeners()
    }

    actual fun release() {
        if (handle != Kanvas.NULL.toLong()) {
            glfwDestroyWindow(handle)
            handle = Kanvas.NULL.toLong()
        }
    }

    actual fun isClosed(): Boolean = glfwWindowShouldClose(handle)

    actual fun applySwapChain() {
        glfwSwapBuffers(handle)
    }

    actual inline fun pollEvents(crossinline block: () -> Unit) {
        glfwPollEvents()
    }

    actual fun setCurrent() {
        glfwMakeContextCurrent(handle)
    }

    private fun initEventListeners() {
        glfwSetWindowSizeCallback(handle) { window, width, height ->
            eventListeners.forEach { it.onWindowResized(width, height) }
        }

        glfwSetWindowPosCallback(handle) { window, x, y ->
            eventListeners.forEach { it.onWindowMoved(x, y) }
        }

        glfwSetKeyCallback(handle) { window, key, scancode, action, mods ->
            when (action) {
                GLFW_PRESS -> eventListeners.forEach { it.onKeyPressed(key.toKeyCode(), false) }
                GLFW_REPEAT -> eventListeners.forEach { it.onKeyPressed(key.toKeyCode(), true) }
                GLFW_RELEASE -> eventListeners.forEach { it.onKeyReleased(key.toKeyCode()) }
            }
        }

        glfwSetCharCallback(handle) { window, codepoint ->
            eventListeners.forEach { it.onKeyTyped(Char(codepoint)) }
        }

        glfwSetMouseButtonCallback(handle) { window, button, action, mods ->
            when (action) {
                GLFW_PRESS -> eventListeners.forEach { it.onMousePressed(button.toMouseCode(), false) }
                GLFW_REPEAT -> eventListeners.forEach { it.onMousePressed(button.toMouseCode(), true) }
                GLFW_RELEASE -> eventListeners.forEach { it.onMouseReleased(button.toMouseCode()) }
            }
        }

        glfwSetCursorPosCallback(handle) { window, x, y ->
            eventListeners.forEach { it.onMouseMove(x, y) }
        }

        glfwSetScrollCallback(handle) { window, x, y ->
            eventListeners.forEach { it.onMouseScroll(x, y) }
        }
    }

    private fun Int.toKeyCode(): KeyCode {
        return when (this) {
            GLFW_KEY_SPACE -> KeyCode.Space
            GLFW_KEY_WORLD_1 -> KeyCode.World1
            GLFW_KEY_WORLD_2 -> KeyCode.World2
            GLFW_KEY_ESCAPE -> KeyCode.Esc
            GLFW_KEY_ENTER -> KeyCode.Enter
            GLFW_KEY_TAB -> KeyCode.Tab
            GLFW_KEY_BACKSPACE -> KeyCode.Backspace
            GLFW_KEY_INSERT -> KeyCode.Insert
            GLFW_KEY_DELETE -> KeyCode.Delete
            GLFW_KEY_RIGHT -> KeyCode.Right
            GLFW_KEY_LEFT -> KeyCode.Left
            GLFW_KEY_DOWN -> KeyCode.Down
            GLFW_KEY_UP -> KeyCode.Up
            GLFW_KEY_PAGE_UP -> KeyCode.PageUp
            GLFW_KEY_PAGE_DOWN -> KeyCode.PageDown
            GLFW_KEY_HOME -> KeyCode.Home
            GLFW_KEY_END -> KeyCode.End
            GLFW_KEY_CAPS_LOCK -> KeyCode.CapsLock
            GLFW_KEY_SCROLL_LOCK -> KeyCode.ScrollLock
            GLFW_KEY_NUM_LOCK -> KeyCode.NumLock
            GLFW_KEY_PRINT_SCREEN -> KeyCode.PrintScreen
            GLFW_KEY_PAUSE -> KeyCode.Pause
            GLFW_KEY_F1 -> KeyCode.F1
            GLFW_KEY_F2 -> KeyCode.F2
            GLFW_KEY_F3 -> KeyCode.F3
            GLFW_KEY_F4 -> KeyCode.F4
            GLFW_KEY_F5 -> KeyCode.F5
            GLFW_KEY_F6 -> KeyCode.F6
            GLFW_KEY_F7 -> KeyCode.F7
            GLFW_KEY_F8 -> KeyCode.F8
            GLFW_KEY_F9 -> KeyCode.F9
            GLFW_KEY_F10 -> KeyCode.F10
            GLFW_KEY_F11 -> KeyCode.F11
            GLFW_KEY_F12 -> KeyCode.F12
            GLFW_KEY_KP_0 -> KeyCode.KP0
            GLFW_KEY_KP_1 -> KeyCode.KP1
            GLFW_KEY_KP_2 -> KeyCode.KP2
            GLFW_KEY_KP_3 -> KeyCode.KP3
            GLFW_KEY_KP_4 -> KeyCode.KP4
            GLFW_KEY_KP_5 -> KeyCode.KP5
            GLFW_KEY_KP_6 -> KeyCode.KP6
            GLFW_KEY_KP_7 -> KeyCode.KP7
            GLFW_KEY_KP_8 -> KeyCode.KP8
            GLFW_KEY_KP_9 -> KeyCode.KP9
            GLFW_KEY_KP_DECIMAL -> KeyCode.KPDecimal
            GLFW_KEY_KP_DIVIDE -> KeyCode.KPDivide
            GLFW_KEY_KP_MULTIPLY -> KeyCode.KPMultiply
            GLFW_KEY_KP_SUBTRACT -> KeyCode.KPSubtract
            GLFW_KEY_KP_ADD -> KeyCode.KPAdd
            GLFW_KEY_KP_ENTER -> KeyCode.KPEnter
            GLFW_KEY_KP_EQUAL -> KeyCode.KPEqual
            GLFW_KEY_LEFT_SHIFT -> KeyCode.LeftShift
            GLFW_KEY_LEFT_CONTROL -> KeyCode.LeftControl
            GLFW_KEY_LEFT_ALT -> KeyCode.LeftAlt
            GLFW_KEY_LEFT_SUPER -> KeyCode.LeftSuper
            GLFW_KEY_RIGHT_SHIFT -> KeyCode.RightShift
            GLFW_KEY_RIGHT_CONTROL -> KeyCode.RightControl
            GLFW_KEY_RIGHT_ALT -> KeyCode.RightAlt
            GLFW_KEY_RIGHT_SUPER -> KeyCode.RightSuper
            GLFW_KEY_MENU -> KeyCode.Menu
            else -> KeyCode.Null
        }
    }

    private fun Int.toMouseCode(): MouseCode {
        return when (this) {
            GLFW_MOUSE_BUTTON_MIDDLE -> MouseCode.Middle
            GLFW_MOUSE_BUTTON_LEFT -> MouseCode.Left
            GLFW_MOUSE_BUTTON_RIGHT -> MouseCode.Right
            else -> MouseCode.Null
        }
    }

    actual fun onMotionEvent(event: Any?) = Unit

}