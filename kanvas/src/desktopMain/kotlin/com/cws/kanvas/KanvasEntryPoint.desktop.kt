package com.cws.kanvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.cws.klog.KLog
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel

class GLPanel(
    private var x: Int,
    private var y: Int,
    private var width: Int,
    private var height: Int
) : JPanel() {

    private var buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    private var pixels = IntArray(width * height)

    @Volatile
    private var newX = x
    @Volatile
    private var newY = y
    @Volatile
    private var newWidth = width
    @Volatile
    private var newHeight = height

    init {
        Thread {
            if (!glfwInit()) throw IllegalStateException("Unable to initialize GLFW")

            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
            val window = glfwCreateWindow(width, height, "Hidden", 0, 0)
            if (window == 0L) throw RuntimeException("Failed to create GLFW window")

            glfwMakeContextCurrent(window)
            GL.createCapabilities()

            glViewport(0, 0, width, height)

            while (!glfwWindowShouldClose(window)) {
                updateSize()

                glClearColor(0f, 0f, 0f, 1f)
                glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

                glBegin(GL_TRIANGLES)
                glColor3f(1f, 0f, 0f)
                glVertex2f(-0.5f, -0.5f)
                glColor3f(0f, 1f, 0f)
                glVertex2f(0.5f, -0.5f)
                glColor3f(0f, 0f, 1f)
                glVertex2f(0f, 0.5f)
                glEnd()

                glFlush()

                writeToBuffer()
                repaint()
                Thread.sleep(16)
            }

            glfwDestroyWindow(window)
            glfwTerminate()
        }.start()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(buffer, 0, 0, null)
    }

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
            buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            pixels = IntArray(width * height)
            glViewport(x, y, width, height)
        }
    }

    private fun writeToBuffer() {
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels)
        buffer.setRGB(0, 0, width, height, pixels, 0, width)
    }

}

actual class KanvasEntryPoint : JFrame {

    actual constructor(
        renderLoop: RenderLoop,
        content: @Composable ((RenderLoop) -> Unit),
    )

}

fun <T : RenderLoop> KanvasApp(
    modifier: Modifier = Modifier,
    renderLoop: T,
    content: @Composable ((T) -> Unit)
) {
    application {
        val state = rememberWindowState(
            position = WindowPosition(renderLoop.x.dp, renderLoop.y.dp),
            width = renderLoop.width.dp,
            height = renderLoop.height.dp,
        )
        var windowState by remember { mutableStateOf<Window?>(null) }
        var frameState by remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(windowState) {
            windowState?.onBitmapChanged = {
                frameState = it
            }
        }

        DisposableEffect(Unit) {
            renderLoop.onWindowCreated = { windowState = it }
            renderLoop.startLoop()
            onDispose {
                renderLoop.stopLoop()
            }
        }

        Window(
            state = state,
            onCloseRequest = ::exitApplication
        ) {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                frameState?.let { frame ->
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        drawImage(frame)
                    }
                }
                content(renderLoop)
            }
        }
    }
}