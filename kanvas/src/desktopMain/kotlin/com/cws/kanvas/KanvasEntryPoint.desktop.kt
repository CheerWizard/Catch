package com.cws.kanvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.skia.Rect

@OptIn(ExperimentalComposeUiApi::class)
fun <T : RenderLoop> KanvasApp(
    modifier: Modifier = Modifier,
    renderLoop: T,
    content: @Composable ((T) -> Unit)
) {
    application(exitProcessOnExit = true) {
        val composeWindow = rememberWindowState(
            position = WindowPosition(renderLoop.x.dp, renderLoop.y.dp),
            width = renderLoop.width.dp,
            height = renderLoop.height.dp,
        )
        var window by remember { mutableStateOf<Window?>(null) }
        var windowBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(window) {
            window?.onBitmapChanged = { windowBitmap = it }
        }

        DisposableEffect(Unit) {
            renderLoop.onWindowCreated = { window = it }
            renderLoop.startLoop()
            onDispose {
                renderLoop.stopLoop()
            }
        }

        LaunchedEffect(composeWindow) {
            window?.composeState = composeWindow
        }

        Window(
            state = composeWindow,
            onCloseRequest = {
                window?.onWindowClose()
                exitApplication()
            },
            onPreviewKeyEvent = { window?.onKeyEvent(it) ?: false }
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .onPointerEvent(PointerEventType.Press) {
                        it.button?.let { btn ->
                            window?.onMousePress(btn)
                        }
                    }
                    .onPointerEvent(PointerEventType.Release) {
                        it.button?.let { btn ->
                            window?.onMouseRelease(btn)
                        }
                    }
                    .onPointerEvent(PointerEventType.Move) {
                        val (x, y) = it.changes.first().position
                        window?.onMouseMove(x, y)
                    }
                    .onPointerEvent(PointerEventType.Scroll) {
                        val (x, y) = it.changes.first().scrollDelta
                        window?.onMouseScroll(x, y)
                    }
            ) {
                windowBitmap?.let { bitmap ->
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        drawImage(bitmap)
                    }
                }
                content(renderLoop)
            }
        }
    }
}