package com.cws.kanvas

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.atomicfu.locks.ReentrantLock
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.Data
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.glReadPixels
import org.lwjgl.opengl.GL11.glViewport
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseEvent.*
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.swing.JPanel

actual typealias WindowID = Long

actual class Window : BaseWindow, JPanel {

    actual companion object {
        private var libraryInitialized: Boolean = false

        actual fun free() {
            glfwTerminate()
        }
    }

    actual override val eventListeners: MutableSet<EventListener> = mutableSetOf()
    actual override val events: ArrayDeque<Any> = ArrayDeque()
    actual override val lock: ReentrantLock = ReentrantLock()

    var handle: WindowID = Kanvas.NULL.toLong()
        private set

    private var x: Int = 0
    private var y: Int = 0
    private var width: Int = 800
    private var height: Int = 600

    private var pixelsInt = IntArray(width * height)
    private var pixelsBytes = ByteArray(pixelsInt.size * 4)

    lateinit var onBitmapChanged: (ImageBitmap) -> Unit

    @Volatile
    private var newX = 0
    @Volatile
    private var newY = 0
    @Volatile
    private var newWidth = 0
    @Volatile
    private var newHeight = 0

    actual constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        title: String
    ) {
        if (!libraryInitialized) {
            System.setProperty("org.lwjgl.glfw.libdecor", "false");
            if (!glfwInit()) {
                throw RuntimeException("Failed to initialize GLFW")
            }
            libraryInitialized = true
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)

        handle = glfwCreateWindow(width, height, title, 0, 0)
        if (handle == Kanvas.NULL.toLong()) {
            throw RuntimeException("Failed to create GLFW window")
        }

        newX = x
        newY = y
        newWidth = width
        newHeight = height

        setCurrent()
        GL.createCapabilities()
        updateSize()
        initEventListeners()

        isFocusable = true
        requestFocusInWindow()
    }

    actual fun release() {
        if (handle != Kanvas.NULL.toLong()) {
            glfwDestroyWindow(handle)
            handle = Kanvas.NULL.toLong()
        }
    }

    actual fun isClosed(): Boolean = glfwWindowShouldClose(handle)

    actual fun applySwapChain() {
        // frame is rendered offscreen
        // instead of explicit swap chain, we write pixels into Java Swing panel
        updateImageBuffer()
        updateSize()
    }

    private fun createBitmap(): ImageBitmap {
        for (i in pixelsInt.indices) {
            val p = pixelsInt[i]

            val a = (p ushr 24) and 0xFF
            val r = ((p ushr 16) and 0xFF) * a / 255
            val g = ((p ushr 8) and 0xFF) * a / 255
            val b = (p and 0xFF) * a / 255

            pixelsBytes[i * 4 + 0] = r.toByte() // R
            pixelsBytes[i * 4 + 1] = g.toByte() // G
            pixelsBytes[i * 4 + 2] = b.toByte() // B
            pixelsBytes[i * 4 + 3] = a.toByte() // A
        }

        val data = Data.makeFromBytes(pixelsBytes)

        val imageInfo = ImageInfo.makeN32(width, height, ColorAlphaType.PREMUL)

        val image = Image.makeRaster(imageInfo, data.bytes, width * 4)

        return image.toComposeImageBitmap()
    }

    actual fun setCurrent() {
        glfwMakeContextCurrent(handle)
    }

    actual fun setSurface(surface: Any?) = Unit

    override fun setBounds(x: Int, y: Int, width: Int, height: Int) {
        super.setBounds(x, y, width, height)
        newX = x
        newY = y
        newWidth = width
        newHeight = height
    }

    private fun updateSize() {
        if (x != newX || y != newY || width != newWidth || height != newHeight) {
            this.x = newX
            this.y = newY
            this.width = newWidth
            this.height = newHeight
            pixelsInt = IntArray(width * height)
            pixelsBytes = ByteArray(pixelsInt.size * 4)
            glViewport(x, y, width, height)
            glfwSetWindowPos(handle, x, y)
            glfwSetWindowSize(handle, width, height)
            eventListeners.forEach { it.onWindowMoved(x, y) }
            eventListeners.forEach { it.onWindowResized(width, height) }
        }
    }

    private fun updateImageBuffer() {
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixelsInt)
        if (::onBitmapChanged.isInitialized) {
            onBitmapChanged(createBitmap())
        }
    }

    private fun initEventListeners() {
        addKeyListener(object : KeyListener {
            override fun keyTyped(key: KeyEvent?) {
                key ?: return
                eventListeners.forEach { it.onKeyTyped(key.keyChar) }
            }
            override fun keyPressed(key: KeyEvent?) {
                key ?: return
                eventListeners.forEach { it.onKeyPressed(key.keyCode.toKeyCode(), false) }
            }
            override fun keyReleased(key: KeyEvent?) {
                key ?: return
                eventListeners.forEach { it.onKeyReleased(key.keyCode.toKeyCode()) }
            }
        })

        addMouseListener(object : MouseListener {
            override fun mouseClicked(button: MouseEvent?) {
                button ?: return
                eventListeners.forEach { it.onMousePressed(button.button.toMouseCode(), false) }
            }
            override fun mousePressed(button: MouseEvent?) {
                button ?: return
                eventListeners.forEach { it.onMousePressed(button.button.toMouseCode(), false) }
            }
            override fun mouseReleased(button: MouseEvent?) {
                button ?: return
                eventListeners.forEach { it.onMouseReleased(button.button.toMouseCode()) }
            }
            override fun mouseEntered(button: MouseEvent?) {
                button ?: return
            }
            override fun mouseExited(button: MouseEvent?) {
                button ?: return
            }
        })

        addMouseMotionListener(object : MouseMotionListener {
            override fun mouseDragged(cursor: MouseEvent?) {
                cursor ?: return
                eventListeners.forEach { it.onMouseMove(cursor.x.toDouble(), cursor.y.toDouble()) }
            }
            override fun mouseMoved(cursor: MouseEvent?) {
                cursor ?: return
                eventListeners.forEach { it.onMouseMove(cursor.x.toDouble(), cursor.y.toDouble()) }
            }
        })

        addMouseWheelListener { cursor ->
            cursor ?: return@addMouseWheelListener
            eventListeners.forEach { it.onMouseScroll(0.0, cursor.wheelRotation * cursor.scrollAmount.toDouble()) }
        }
    }

    private fun Int.toKeyCode(): KeyCode {
        return when (this) {
            VK_SPACE -> KeyCode.Space
//            VK_WORLD_1 -> KeyCode.World1
//            VK_WORLD_2 -> KeyCode.World2
            VK_ESCAPE -> KeyCode.Esc
            VK_ENTER -> KeyCode.Enter
            VK_TAB -> KeyCode.Tab
            VK_BACK_SPACE -> KeyCode.Backspace
            VK_INSERT -> KeyCode.Insert
            VK_DELETE -> KeyCode.Delete
            VK_RIGHT -> KeyCode.Right
            VK_LEFT -> KeyCode.Left
            VK_DOWN -> KeyCode.Down
            VK_UP -> KeyCode.Up
            VK_PAGE_UP -> KeyCode.PageUp
            VK_PAGE_DOWN -> KeyCode.PageDown
            VK_HOME -> KeyCode.Home
            VK_END -> KeyCode.End
            VK_CAPS_LOCK -> KeyCode.CapsLock
            VK_SCROLL_LOCK -> KeyCode.ScrollLock
            VK_NUM_LOCK -> KeyCode.NumLock
            VK_PRINTSCREEN -> KeyCode.PrintScreen
            VK_PAUSE -> KeyCode.Pause
            VK_F1 -> KeyCode.F1
            VK_F2 -> KeyCode.F2
            VK_F3 -> KeyCode.F3
            VK_F4 -> KeyCode.F4
            VK_F5 -> KeyCode.F5
            VK_F6 -> KeyCode.F6
            VK_F7 -> KeyCode.F7
            VK_F8 -> KeyCode.F8
            VK_F9 -> KeyCode.F9
            VK_F10 -> KeyCode.F10
            VK_F11 -> KeyCode.F11
            VK_F12 -> KeyCode.F12
            VK_NUMPAD0 -> KeyCode.KP0
            VK_NUMPAD1 -> KeyCode.KP1
            VK_NUMPAD2 -> KeyCode.KP2
            VK_NUMPAD3 -> KeyCode.KP3
            VK_NUMPAD4 -> KeyCode.KP4
            VK_NUMPAD5 -> KeyCode.KP5
            VK_NUMPAD6 -> KeyCode.KP6
            VK_NUMPAD7 -> KeyCode.KP7
            VK_NUMPAD8 -> KeyCode.KP8
            VK_NUMPAD9 -> KeyCode.KP9
            VK_DECIMAL -> KeyCode.KPDecimal
            VK_DIVIDE -> KeyCode.KPDivide
            VK_MULTIPLY -> KeyCode.KPMultiply
            VK_SUBTRACT -> KeyCode.KPSubtract
            VK_ADD -> KeyCode.KPAdd
            VK_ENTER -> KeyCode.KPEnter
            VK_EQUALS -> KeyCode.KPEqual
            VK_SHIFT -> KeyCode.LeftShift
            VK_CONTROL -> KeyCode.LeftControl
//            VK_LEFT_ALT -> KeyCode.LeftAlt
//            VK_LEFT_SUPER -> KeyCode.LeftSuper
//            VK_RIGHT_SHIFT -> KeyCode.RightShift
//            VK_RIGHT_CONTROL -> KeyCode.RightControl
//            VK_RIGHT_ALT -> KeyCode.RightAlt
//            VK_RIGHT_SUPER -> KeyCode.RightSuper
            VK_CONTEXT_MENU -> KeyCode.Menu
            else -> KeyCode.Null
        }
    }

    private fun Int.toMouseCode(): MouseCode {
        return when (this) {
            BUTTON3 -> MouseCode.Middle
            BUTTON1 -> MouseCode.Left
            BUTTON2 -> MouseCode.Right
            else -> MouseCode.Null
        }
    }

}